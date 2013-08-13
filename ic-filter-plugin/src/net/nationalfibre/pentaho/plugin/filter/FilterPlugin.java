package net.nationalfibre.pentaho.plugin.filter;

import java.net.URI;
import java.net.URISyntaxException;

import net.nationalfibre.filter.Data;
import net.nationalfibre.filter.DataFilter;
import net.nationalfibre.filter.FilterConfig;
import net.nationalfibre.filter.FilterFactory;
import net.nationalfibre.filter.ProviderType;
import net.nationalfibre.filter.FilterType;

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
import org.pentaho.di.trans.step.StepListener;

/**
 * Pentaho filter plugin
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class FilterPlugin extends BaseStep implements StepInterface
{
    /**
     * Filter data
     */
    private FilterPluginData data;

    /**
     * Filter meta data
     */
    private FilterPluginMeta meta;

    /**
     * Data filter
     */
    private DataFilter filter;

    /**
     * {@inheritDoc}
     */
    public FilterPlugin(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans)
    {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
    {
        meta = (FilterPluginMeta) smi;
        data = (FilterPluginData) sdi;

        Object[] r = getRow(); // get row, blocks when needed! no more input to be expected...

        if (r == null) {
            setOutputDone();
            return false;
        }

        if (first) {
            first = false;

            Integer elements     = Integer.parseInt(meta.getElements());
            Integer lookups      = Integer.parseInt(meta.getLookups());
            Integer division     = Integer.parseInt(meta.getDivision());
            Float probability    = Float.parseFloat(meta.getProbability());
            ProviderType pType   = ProviderType.VFS;
            FilterType   fType   = FilterType.MAP;
            String hashFieldName = meta.getHash();
            String timeFieldName = meta.getTime();
            String uriStr        = meta.getUri();
            URI uri              = null;

            if (hashFieldName == null) {
                throw new FilterException("Unable to retrieve hash field name");
            }

            if (timeFieldName == null) {
                throw new FilterException("Unable to retrieve timestamp field name");
            }

            if (uriStr == null) {
                throw new FilterException("Unable to retrieve filter uri");
            }

            if ("BLOOM".equals(meta.getFilter())) {
                fType = FilterType.BLOOM;
            }

            try {
                uri = new URI(uriStr);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            FilterConfig config = FilterConfig.create()
                .withFalsePositiveProbability(probability)
                .withExpectedNumberOfElements(lookups)
                .withNumberOfLookups(lookups)
                .withTimeDivision(division)
                .withProvider(pType)
                .withFilter(fType)
                .withURI(uriStr);

            filter = FilterFactory.createFilter(config);

            log.logDetailed(String.format("Filter URI (%s)", uriStr));
            log.logDetailed(String.format("Filter Type (%s)", fType));
            log.logDetailed(String.format("Provider (%s)", pType));

            if (fType == FilterType.BLOOM) {
                log.logDetailed(String.format("Expected Number Of Elements (%s)", elements));
                log.logDetailed(String.format("False Positive Probability (%s)", probability));
            }

            log.logDetailed(String.format("Time Div (%s)", division));
            log.logDetailed(String.format("Number Of Lookups (%s)", lookups));

            // clone the input row structure and place it in our data object
            data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
            // use meta.getFields() to change it, so it reflects the output row structure 
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);;

            // get field index
            data.hashFieldIndex  = data.outputRowMeta.indexOfValue(hashFieldName);
            data.timeFieldIndex  = data.outputRowMeta.indexOfValue(timeFieldName);

            if (data.hashFieldIndex == null || data.hashFieldIndex < 0) {
                throw new FilterException("Unable to retrieve hash field : " + hashFieldName);
            }

            if (data.timeFieldIndex == null || data.hashFieldIndex < 0) {
                throw new FilterException("Unable to retrieve time field : " + timeFieldName);
            }
        }

        if (r.length < data.hashFieldIndex || r.length < data.timeFieldIndex) {
            String putErrorMessage = getLinesRead() + " - Ignore empty row";

            if (isDebug()) {
                log.logDebug(putErrorMessage);
            }

            putError(getInputRowMeta(), r, 1, putErrorMessage, null, "ICFilterPlugin001");

            return true;
        }

        if (r[data.hashFieldIndex] == null || r[data.timeFieldIndex] == null) {
            String putErrorMessage = getLinesRead() + " - Ignore null row";

            if (isDebug()) {
                log.logDebug(putErrorMessage);
            }

            putError(getInputRowMeta(), r, 1, putErrorMessage, null, "ICFilterPlugin002");

            return true;
        }

        data.hashValue  = String.valueOf(r[data.hashFieldIndex]);
        data.timeValue  = Long.parseLong(String.valueOf(r[data.timeFieldIndex]));
        data.filterData = new Data(data.hashValue, data.timeValue);

        if ( ! filter.add(data.filterData)) {

            if (isDebug()) {
                log.logDebug(getLinesRead() + " - Ignore row : " + data.hashValue);    
            }

            return true;
        }

        if (isDebug()) {
            log.logDebug(getLinesRead() + " - Accept row : " + data.hashValue);    
        }

        putRow(data.outputRowMeta, r);

        return true;
    }

    /**
     * Flush filter files
     */
    public void flushFilter()
    {
        logMinimal("Flush filters invoked");

        if (filter != null) {
            filter.flush();
            logMinimal("Flush filters complete");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean init(StepMetaInterface smi, StepDataInterface sdi)
    {
        meta = (FilterPluginMeta) smi;
        data = (FilterPluginData) sdi;

        return super.init(smi, sdi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose(StepMetaInterface smi, StepDataInterface sdi)
    {
        log.logDebug("dispose");

        meta = (FilterPluginMeta) smi;
        data = (FilterPluginData) sdi;

        flushFilter();

        super.dispose(smi, sdi);
    }
}