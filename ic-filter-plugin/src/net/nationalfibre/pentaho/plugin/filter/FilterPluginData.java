package net.nationalfibre.pentaho.plugin.filter;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class FilterPluginData extends BaseStepData implements StepDataInterface
{

    public RowMetaInterface outputRowMeta;

    public FilterPluginData()
    {
        super();
    }
}
