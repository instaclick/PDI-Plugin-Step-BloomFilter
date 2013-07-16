package net.nationalfibre.pentaho.plugin.filter;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

public class FilterPluginMeta extends BaseStepMeta implements StepMetaInterface {

    private static String FIELD_PROBABLILITY = "probability";
    private static String FIELD_ELEMENTS     = "elements";
    private static String FIELD_LOOKUPS      = "lookups";
    private static String FIELD_DIVISION     = "division";
    private static String FIELD_HASH         = "hash";
    private static String FIELD_TIME         = "time";
    private static String FIELD_URI          = "uri";
    private String elements                  = "1000";
    private String probability               = "0.1";
    private String uri                       = "tmp://ic-filter/";
    private String division                  = "60";
    private String lookups                   = "1440";
    private String hash                      = "hash";
    private String time                      = "timestamp";

    public FilterPluginMeta() {
        super();
    }

    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
        return new FilterPluginDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new FilterPlugin(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new FilterPluginData();
    }

    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) {

        CheckResult prevSizeCheck = (prev == null || prev.size() == 0)
                ? new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta)
                : new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is connected to previous one, receiving " + prev.size() + " fields", stepMeta);

        /// See if we have input streams leading to this step!
        CheckResult inputLengthCheck = (input.length > 0)
                ? new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta)
                : new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);

        CheckResult hashFieldCheck = ((prev == null) || (prev.indexOfValue(getHash()) < 0))
                ? new CheckResult(CheckResult.TYPE_RESULT_ERROR, "Hash field not found.", stepMeta)
                : new CheckResult(CheckResult.TYPE_RESULT_OK, "Hash field found.", stepMeta);

        CheckResult timeFieldCheck = ((prev == null) || (prev.indexOfValue(getTime()) < 0))
                ? new CheckResult(CheckResult.TYPE_RESULT_ERROR, "Timestamp field not found.", stepMeta)
                : new CheckResult(CheckResult.TYPE_RESULT_OK, "Timestamp field found.", stepMeta);

        remarks.add(prevSizeCheck);
        remarks.add(inputLengthCheck);
        remarks.add(hashFieldCheck);
        remarks.add(timeFieldCheck);
    }

    @Override
    public String getXML() {
        final StringBuilder bufer = new StringBuilder();

        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_PROBABLILITY, getProbability()));
        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_ELEMENTS, getElements()));
        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_DIVISION, getDivision()));
        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_LOOKUPS, getLookups()));
        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_HASH, getHash()));
        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_TIME, getTime()));
        bufer.append("   ").append(XMLHandler.addTagValue(FIELD_URI, getUri()));

        return bufer.toString();
    }

    @Override
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
        try {

            setProbability(XMLHandler.getTagValue(stepnode, FIELD_PROBABLILITY));
            setElements(XMLHandler.getTagValue(stepnode, FIELD_ELEMENTS));
            setDivision(XMLHandler.getTagValue(stepnode, FIELD_DIVISION));
            setLookups(XMLHandler.getTagValue(stepnode, FIELD_LOOKUPS));
            setHash(XMLHandler.getTagValue(stepnode, FIELD_HASH));
            setTime(XMLHandler.getTagValue(stepnode, FIELD_TIME));
            setUri(XMLHandler.getTagValue(stepnode, "uri"));

        } catch (Exception e) {
            throw new KettleXMLException("Unable to read step info from XML node", e);
        }
    }

    @Override
    public void readRep(Repository rep, ObjectId idStep, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
        try {

            setProbability(rep.getStepAttributeString(idStep, FIELD_PROBABLILITY));
            setElements(rep.getStepAttributeString(idStep, FIELD_ELEMENTS));
            setDivision(rep.getStepAttributeString(idStep, FIELD_DIVISION));
            setLookups(rep.getStepAttributeString(idStep, FIELD_LOOKUPS));
            setHash(rep.getStepAttributeString(idStep, FIELD_HASH));
            setTime(rep.getStepAttributeString(idStep, FIELD_TIME));
            setUri(rep.getStepAttributeString(idStep, "uri"));

        } catch (KettleDatabaseException dbe) {
            throw new KettleException("error reading step with id_step=" + idStep + " from the repository", dbe);
        } catch (Exception e) {
            throw new KettleException("Unexpected error reading step with id_step=" + idStep + " from the repository", e);
        }
    }

    @Override
    public void saveRep(Repository rep, ObjectId idTransformation, ObjectId idStep) throws KettleException {
        try {

            rep.saveStepAttribute(idTransformation, idStep, FIELD_PROBABLILITY, getProbability());
            rep.saveStepAttribute(idTransformation, idStep, FIELD_ELEMENTS, getElements());
            rep.saveStepAttribute(idTransformation, idStep, FIELD_DIVISION, getDivision());
            rep.saveStepAttribute(idTransformation, idStep, FIELD_LOOKUPS, getLookups());
            rep.saveStepAttribute(idTransformation, idStep, FIELD_HASH, getHash());
            rep.saveStepAttribute(idTransformation, idStep, FIELD_TIME, getTime());
            rep.saveStepAttribute(idTransformation, idStep, FIELD_URI, getUri());

        } catch (KettleDatabaseException dbe) {
            throw new KettleException("Unable to save step information to the repository, id_step=" + idStep, dbe);
        }
    }

    @Override
    public void setDefault() {
        this.elements    = "1000";
        this.probability = "0.1";

        this.uri        = "tmp://ic-filter/";
        this.division   = "60";
        this.lookups    = "1440";
        this.hash       = "hash";
        this.time       = "timestamp";
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String expectedNumberOfElements) {
        this.elements = expectedNumberOfElements;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String falsePositiveProbability) {
        this.probability = falsePositiveProbability;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String timeDivision) {
        this.division = timeDivision;
    }

    public String getLookups() {
        return lookups;
    }

    public void setLookups(String numberOfLookups) {
        this.lookups = numberOfLookups;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hashFieldName) {
        this.hash = hashFieldName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String timeFieldName) {
        this.time = timeFieldName;
    }
}
