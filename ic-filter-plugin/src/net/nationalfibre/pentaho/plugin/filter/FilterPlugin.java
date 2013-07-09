package net.nationalfibre.pentaho.plugin.filter;

import net.nationalfibre.filter.Data;
import net.nationalfibre.filter.DataFilter;
import net.nationalfibre.filter.FilterConfig;
import net.nationalfibre.filter.FilterFactory;
import net.nationalfibre.filter.ProviderType;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

public class FilterPlugin extends BaseStep implements StepInterface {
	
	private FilterPluginData data;
	private FilterPluginMeta meta;
	
	private DataFilter filter;
	private Data filterData;

	private Integer hashFieldIndex = null;
	private Integer timeFielIndex  = null;
	private String hashValue 	   = null;
	private Long timeValue  	   = null;

	public FilterPlugin(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		meta = (FilterPluginMeta) smi;
		data = (FilterPluginData) sdi;

		Object[] r = getRow(); // get row, blocks when needed! no more input to be expected...

		if (r == null && filter != null) {

			this.log.logDebug("Flush filters to vfs directory");

			filter.flush();
		}

		if (r == null) {
			setOutputDone();

			return false;
		}

		if (first) {
			first = false;

			Integer elements	 	= Integer.parseInt(meta.getElements());
			Integer lookups  		= Integer.parseInt(meta.getLookups());
			Integer division  		= Integer.parseInt(meta.getDivision());
			Float probability  		= Float.parseFloat(meta.getProbability());
			String hashFieldName 	= meta.getHash();
			String timeFieldName 	= meta.getTime();
			String uri  			= meta.getUri();

			if (hashFieldName == null) {
				throw new FilterException("Unable to retrieve hash field name");
			}

			if (timeFieldName == null) {
				throw new FilterException("Unable to retrieve timestamp field name");
			}

			if (uri == null) {
				throw new FilterException("Unable to retrieve filter uri");
			}

			FilterConfig config = FilterConfig.create()
				.withFalsePositiveProbability(probability)
				.withExpectedNumberOfElements(lookups)
				.withProvider(ProviderType.VFS)
				.withNumberOfLookups(lookups)
				.withTimeDivision(division)
				.withURI(uri);

			filter = FilterFactory.createFilter(config);

			log.logDetailed(String.format("Filter URI (%s)", uri));
			log.logDetailed(String.format("Expected Number Of Elements (%s)", elements));
			log.logDetailed(String.format("False Positive Probability (%s)", probability));
			log.logDetailed(String.format("Time Div (%s)", division));
			log.logDetailed(String.format("Number Of Lookups (%s)", lookups));

			data.outputRowMeta  = (RowMetaInterface) getInputRowMeta().clone();
			hashFieldIndex 		= data.outputRowMeta.indexOfValue(hashFieldName);
			timeFielIndex 		= data.outputRowMeta.indexOfValue(timeFieldName);

			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			if (hashFieldIndex == null || hashFieldIndex< 0) {
				throw new FilterException("Unable to retrieve hash field : " + hashFieldName);
			}

			if (timeFielIndex == null || timeFielIndex< 0) {
				throw new FilterException("Unable to retrieve time field : " + timeFieldName);
			}
		}

		hashValue  = String.valueOf(r[hashFieldIndex]);
		timeValue  = Long.parseLong(String.valueOf(r[timeFielIndex]));
		filterData = new Data(hashValue, timeValue);

		if ( ! filter.add(filterData)) {
			log.logDebug(getLinesRead() + " - Ignore row : " + filterData.getHash());

			return true;
		}

		putRow(data.outputRowMeta, r);

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (FilterPluginMeta) smi;
		data = (FilterPluginData) sdi;

		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (FilterPluginMeta) smi;
		data = (FilterPluginData) sdi;

		super.dispose(smi, sdi);
	}

	// Run is were the action happens!
	public void run() {
		logBasic("Starting to run...");
		try {

			while (processRow(meta, data) && ! isStopped());

		} catch (Exception e) {
			logError("Unexpected error : " + e.toString());
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		} finally {
			dispose(meta, data);
			logBasic("Finished, processing " + getLinesRead() + " rows");
			markStop();
		}
	}
}