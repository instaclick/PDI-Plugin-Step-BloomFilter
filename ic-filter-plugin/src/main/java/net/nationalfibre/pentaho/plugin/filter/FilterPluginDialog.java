package net.nationalfibre.pentaho.plugin.filter;

import net.nationalfibre.filter.FilterType;
import net.nationalfibre.filter.HashFunctionType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import static net.nationalfibre.pentaho.plugin.filter.Messages.getString;

/**
 * Pentaho filter plugin dialog
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class FilterPluginDialog extends BaseStepDialog implements StepDialogInterface
{

    private FilterPluginMeta input;
    private Label labelHash;
    private Text textHash;
    private FormData formHashLabel, formHashText;

    private Label labelTime;
    private Text textTime;
    private FormData formTimeLabel;
    private FormData formTimeText;

    private Label labelElements;
    private Text textElements;
    private FormData formElementsLabel;
    private FormData formElementsText;

    private Label labelProbability;
    private Text textProbability;
    private FormData formProbabilityLabel;
    private FormData formProbabilityText;

    private Label labelLookups;
    private Text textLookups;
    private FormData formLookupsLabel;
    private FormData formLookupsText;

    private Label labelDivision;
    private Text textDivision;
    private FormData formDivisionLabel;
    private FormData formDivisionText;
    
    private Label labelUri;
    private Text textUri;
    private FormData formUriLabel;
    private FormData formUriText;

    private Label    labelFilterType;
    private CCombo   comboFilterType;
    private FormData formFilterTypeLabel;
    private FormData formFilterTypeCombo;

    private Label    labelHashFunction;
    private CCombo   comboHashFunction;
    private FormData formHashFunctionLabel;
    private FormData formHashFunctionCombo;

    private Label labelAlwaysPassRow;
    private Button checkAlwaysPassRow;
    private FormData formAlwaysPassRowLabel;
    private FormData formAlwaysPassRowText;

    private Label labelUniqueField;
    private Text textUniqueField;
    private FormData formUniqueFieldLabel;
    private FormData formUniqueFieldText;

    private Label labelTransactional;
    private Button checkTransactional;
    private FormData formTransactionalLabel;
    private FormData formTransactionalText;

    private static String[] filterTypes = { FilterType.BLOOM.toString(), FilterType.MAP.toString() };
    private static String[] hashFunctionTypes = HashFunctionType.getHashFunctionNames();

    private ModifyListener modifyListener = new ModifyListener() {
        @Override
        public void modifyText(ModifyEvent e) {
            input.setChanged();
        }
    };

    private SelectionAdapter selectionModifyListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            input.setChanged();
        }
    };

    private SelectionAdapter comboFilterTypeListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {

            if (FilterType.BLOOM.toString().equals(comboFilterType.getText())) {
                textProbability.setEnabled(true);
                textElements.setEnabled(true); 

                return;
            }

            textProbability.setEnabled(false);
            textElements.setEnabled(false); 
        }
    };

    private SelectionAdapter checkAlwaysPassRowListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {

            if (checkAlwaysPassRow.getSelection()) {
                textUniqueField.setEnabled(true);

                return;
            }

            textUniqueField.setEnabled(false);
        }
    };


    public FilterPluginDialog(Shell parent, Object in, TransMeta transMeta, String sname)
    {
        super(parent, (BaseStepMeta) in, transMeta, sname);

        input = (FilterPluginMeta) in;
    }

    public String open()
    {
        Shell parent    = getParent();
        Display display = parent.getDisplay();
        shell           = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        props.setLook(shell);
        setShellImage(shell, input);

        changed = input.hasChanged();

        FormLayout formLayout   = new FormLayout();
        formLayout.marginWidth  = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout(formLayout);
        shell.setText(getString("FilterPlugin.Shell.Title"));

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText(getString("FilterPlugin.StepName.Label"));
        props.setLook(wlStepname);

        fdlStepname         = new FormData();
        fdlStepname.left    = new FormAttachment(0, 0);
        fdlStepname.right   = new FormAttachment(middle, -margin);
        fdlStepname.top     = new FormAttachment(0, margin);

        wlStepname.setLayoutData(fdlStepname);
        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(modifyListener);

        fdStepname       = new FormData();
        fdStepname.left  = new FormAttachment(middle, 0);
        fdStepname.top   = new FormAttachment(0, margin);
        fdStepname.right = new FormAttachment(100, 0);

        wStepname.setLayoutData(fdStepname);

        // Filter Type
        labelFilterType=new Label(shell, SWT.RIGHT);
        labelFilterType.setText(getString("FilterPlugin.FilterType.Label"));
        props.setLook(labelFilterType);

        formFilterTypeLabel       = new FormData();
        formFilterTypeLabel.left  = new FormAttachment(0, 0);
        formFilterTypeLabel.top   = new FormAttachment(wStepname, margin);
        formFilterTypeLabel.right = new FormAttachment(middle, 0);

        labelFilterType.setLayoutData(formFilterTypeLabel);

        comboFilterType = new CCombo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.READ_ONLY);

        comboFilterType.setToolTipText(getString("FilterPlugin.FilterType.Label"));
        comboFilterType.addSelectionListener(comboFilterTypeListener);
        comboFilterType.addSelectionListener(selectionModifyListener);
        comboFilterType.setItems(filterTypes);
        props.setLook(comboFilterType);

        formFilterTypeCombo      = new FormData();
        formFilterTypeCombo.left = new FormAttachment(middle, margin);
        formFilterTypeCombo.top  = new FormAttachment(wStepname, margin);
        formFilterTypeCombo.right= new FormAttachment(100, 0);

        comboFilterType.setLayoutData(formFilterTypeCombo);

        // Transactional
        labelTransactional = new Label(shell, SWT.RIGHT);
        labelTransactional.setText(getString("FilterPlugin.Transactional.Label"));
        props.setLook(labelTransactional);

        formTransactionalLabel       = new FormData();
        formTransactionalLabel.left  = new FormAttachment(0, 0);
        formTransactionalLabel.right = new FormAttachment(middle, -margin);
        formTransactionalLabel.top   = new FormAttachment(comboFilterType , margin);

        labelTransactional.setLayoutData(formTransactionalLabel);

        checkTransactional = new Button(shell, SWT.CHECK);
        props.setLook(checkTransactional);
        checkTransactional.addSelectionListener(selectionModifyListener);

        formTransactionalText        = new FormData();
        formTransactionalText.left   = new FormAttachment(middle, 0);
        formTransactionalText.right  = new FormAttachment(100, 0);
        formTransactionalText.top    = new FormAttachment(comboFilterType, margin);

        checkTransactional.setLayoutData(formTransactionalText);

        // Hash Function
        labelHashFunction=new Label(shell, SWT.RIGHT);
        labelHashFunction.setText(getString("FilterPlugin.HashFunction.Label"));
        props.setLook(labelHashFunction);

        formHashFunctionLabel       = new FormData();
        formHashFunctionLabel.left  = new FormAttachment(0, 0);
        formHashFunctionLabel.top   = new FormAttachment(checkTransactional, margin);
        formHashFunctionLabel.right = new FormAttachment(middle, 0);

        labelHashFunction.setLayoutData(formHashFunctionLabel);

        comboHashFunction = new CCombo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.READ_ONLY);

        comboHashFunction.setToolTipText(getString("FilterPlugin.HashFunction.Label"));
        comboHashFunction.setItems(HashFunctionType.getHashFunctionNames());
        comboHashFunction.addModifyListener(modifyListener);
        props.setLook(comboHashFunction);
        
        formHashFunctionCombo      = new FormData();
        formHashFunctionCombo.left = new FormAttachment(middle, margin);
        formHashFunctionCombo.top  = new FormAttachment(checkTransactional, margin);
        formHashFunctionCombo.right= new FormAttachment(100, 0);

        comboHashFunction.setLayoutData(formHashFunctionCombo);

        // Unique Field
        labelAlwaysPassRow = new Label(shell, SWT.RIGHT);
        labelAlwaysPassRow.setText(getString("FilterPlugin.AlwaysPassRow.Label"));
        props.setLook(labelAlwaysPassRow);

        formAlwaysPassRowLabel       = new FormData();
        formAlwaysPassRowLabel.left  = new FormAttachment(0, 0);
        formAlwaysPassRowLabel.right = new FormAttachment(middle, -margin);
        formAlwaysPassRowLabel.top   = new FormAttachment(comboHashFunction, margin);

        labelAlwaysPassRow.setLayoutData(formAlwaysPassRowLabel);

        checkAlwaysPassRow = new Button(shell, SWT.CHECK);
        checkAlwaysPassRow.addSelectionListener(checkAlwaysPassRowListener);
        checkAlwaysPassRow.addSelectionListener(selectionModifyListener);
        props.setLook(checkAlwaysPassRow);

        formAlwaysPassRowText        = new FormData();
        formAlwaysPassRowText.left   = new FormAttachment(middle, 0);
        formAlwaysPassRowText.right  = new FormAttachment(100, 0);
        formAlwaysPassRowText.top    = new FormAttachment(comboHashFunction, margin);

        checkAlwaysPassRow.setLayoutData(formAlwaysPassRowText);

         // UniqueField line
        labelUniqueField = new Label(shell, SWT.RIGHT);
        labelUniqueField.setText(getString("FilterPlugin.UniqueField.Label"));
        props.setLook(labelUniqueField);

        formUniqueFieldLabel       = new FormData();
        formUniqueFieldLabel.left  = new FormAttachment(0, 0);
        formUniqueFieldLabel.right = new FormAttachment(middle, -margin);
        formUniqueFieldLabel.top   = new FormAttachment(checkAlwaysPassRow , margin);

        labelUniqueField.setLayoutData(formUniqueFieldLabel);

        textUniqueField = new Text(shell, SWT.MULTI | SWT.LEFT | SWT.BORDER);

        props.setLook(textUniqueField);
        textUniqueField.addModifyListener(modifyListener);

        formUniqueFieldText        = new FormData();
        formUniqueFieldText.left   = new FormAttachment(middle, 0);
        formUniqueFieldText.right  = new FormAttachment(100, 0);
        formUniqueFieldText.top    = new FormAttachment(checkAlwaysPassRow, margin);

        textUniqueField.setLayoutData(formUniqueFieldText);

        // hash line
        labelHash = new Label(shell, SWT.RIGHT);
        labelHash.setText(getString("FilterPlugin.Hash.Label"));
        props.setLook(labelHash);

        formHashLabel       = new FormData();
        formHashLabel.left  = new FormAttachment(0, 0);
        formHashLabel.right = new FormAttachment(middle, -margin);
        formHashLabel.top   = new FormAttachment(labelUniqueField , margin);

        labelHash.setLayoutData(formHashLabel);

        textHash = new Text(shell, SWT.MULTI | SWT.LEFT | SWT.BORDER);

        props.setLook(textHash);
        textHash.addModifyListener(modifyListener);

        formHashText        = new FormData();
        formHashText.left   = new FormAttachment(middle, 0);
        formHashText.right  = new FormAttachment(100, 0);
        formHashText.top    = new FormAttachment(labelUniqueField, margin);

        textHash.setLayoutData(formHashText);

        // time line
        labelTime = new Label(shell, SWT.RIGHT);
        labelTime.setText(getString("FilterPlugin.Time.Label"));
        props.setLook(labelTime);

        formTimeLabel       = new FormData();
        formTimeLabel.left  = new FormAttachment(0, 0);
        formTimeLabel.right = new FormAttachment(middle, -margin);
        formTimeLabel.top   = new FormAttachment(textHash, margin);

        labelTime.setLayoutData(formTimeLabel);
        textTime = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textTime);
        textTime.addModifyListener(modifyListener);

        formTimeText        = new FormData();
        formTimeText.left   = new FormAttachment(middle, 0);
        formTimeText.right  = new FormAttachment(100, 0);
        formTimeText.top    = new FormAttachment(textHash, margin);
        textTime.setLayoutData(formTimeText);

        // FilterSize line
        labelElements = new Label(shell, SWT.RIGHT);
        labelElements.setText(getString("FilterPlugin.Elements.Label"));
        props.setLook(labelElements);

        formElementsLabel       = new FormData();
        formElementsLabel.left  = new FormAttachment(0, 0);
        formElementsLabel.right = new FormAttachment(middle, -margin);
        formElementsLabel.top   = new FormAttachment(textTime, margin);

        labelElements.setLayoutData(formElementsLabel);

        textElements = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textElements);
        textElements.addModifyListener(modifyListener);

        formElementsText        = new FormData();
        formElementsText.left   = new FormAttachment(middle, 0);
        formElementsText.right  = new FormAttachment(100, 0);
        formElementsText.top    = new FormAttachment(textTime, margin);

        textElements.setLayoutData(formElementsText);

        // FilterProbability line
        labelProbability = new Label(shell, SWT.RIGHT);

        labelProbability.setText(getString("FilterPlugin.Probability.Label"));
        props.setLook(labelProbability);

        formProbabilityLabel        = new FormData();
        formProbabilityLabel.left   = new FormAttachment(0, 0);
        formProbabilityLabel.right  = new FormAttachment(middle, -margin);
        formProbabilityLabel.top    = new FormAttachment(textElements, margin);

        labelProbability.setLayoutData(formProbabilityLabel);

        textProbability = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textProbability);
        textProbability.addModifyListener(modifyListener);

        formProbabilityText         = new FormData();
        formProbabilityText.left    = new FormAttachment(middle, 0);
        formProbabilityText.right   = new FormAttachment(100, 0);
        formProbabilityText.top     = new FormAttachment(textElements, margin);

        textProbability.setLayoutData(formProbabilityText);

        // FilterDirectory line
        labelUri = new Label(shell, SWT.RIGHT);

        labelUri.setText(getString("FilterPlugin.Uri.Label"));
        props.setLook(labelUri);

        formUriLabel        = new FormData();
        formUriLabel.left   = new FormAttachment(0, 0);
        formUriLabel.right  = new FormAttachment(middle, -margin);
        formUriLabel.top    = new FormAttachment(textProbability, margin);

        labelUri.setLayoutData(formUriLabel);

        textUri = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textUri);
        textUri.addModifyListener(modifyListener);

        formUriText         = new FormData();
        formUriText.left    = new FormAttachment(middle, 0);
        formUriText.right   = new FormAttachment(100, 0);
        formUriText.top     = new FormAttachment(textProbability, margin);

        textUri.setLayoutData(formUriText);

        // FilterLookups line
        labelLookups = new Label(shell, SWT.RIGHT);

        labelLookups.setText(getString("FilterPlugin.Lookups.Label"));
        props.setLook(labelLookups);

        formLookupsLabel        = new FormData();
        formLookupsLabel.left   = new FormAttachment(0, 0);
        formLookupsLabel.right  = new FormAttachment(middle, -margin);
        formLookupsLabel.top    = new FormAttachment(textUri, margin);

        labelLookups.setLayoutData(formLookupsLabel);

        textLookups = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textLookups);
        textLookups.addModifyListener(modifyListener);

        formLookupsText         = new FormData();
        formLookupsText.left    = new FormAttachment(middle, 0);
        formLookupsText.right   = new FormAttachment(100, 0);
        formLookupsText.top     = new FormAttachment(textUri, margin);

        textLookups.setLayoutData(formLookupsText);

        // FilterDivision line
        labelDivision = new Label(shell, SWT.RIGHT);

        labelDivision.setText(getString("FilterPlugin.Division.Label"));
        props.setLook(labelDivision);

        formDivisionLabel       = new FormData();
        formDivisionLabel.left  = new FormAttachment(0, 0);
        formDivisionLabel.right = new FormAttachment(middle, -margin);
        formDivisionLabel.top   = new FormAttachment(textLookups, margin);

        labelDivision.setLayoutData(formDivisionLabel);

        textDivision = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textDivision);
        textDivision.addModifyListener(modifyListener);

        formDivisionText        = new FormData();
        formDivisionText.left   = new FormAttachment(middle, 0);
        formDivisionText.right  = new FormAttachment(100, 0);
        formDivisionText.top    = new FormAttachment(textLookups, margin);

        textDivision.setLayoutData(formDivisionText);

        // Some buttons
        wOK     = new Button(shell, SWT.PUSH);
        wCancel = new Button(shell, SWT.PUSH);

        wOK.setText(getString("System.Button.OK"));
        wCancel.setText(getString("System.Button.Cancel"));

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, textDivision);

        // Add listeners
        lsCancel = new Listener() {
            public void handleEvent(Event e) {
                cancel();
            }
        };
        lsOK = new Listener() {
            public void handleEvent(Event e) {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, lsCancel);
        wOK.addListener(SWT.Selection, lsOK);

        lsDef = new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };

        wStepname.addSelectionListener(lsDef);
        textHash.addSelectionListener(lsDef);
        textElements.addSelectionListener(lsDef);
        textTime.addSelectionListener(lsDef);
        textLookups.addSelectionListener(lsDef);
        textDivision.addSelectionListener(lsDef);
        textUri.addSelectionListener(lsDef);
        textProbability.addSelectionListener(lsDef);

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });

        // Set the shell size, based upon previous time...
        setSize();

        getData();
        input.setChanged(changed);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return stepname;
    }

    // Read data from input (TextFileInputInfo)
    public void getData()
    {
        wStepname.selectAll();

        if (input.getHash() != null) {
            textHash.setText(input.getHash());
        }

        if (input.getTime() != null) {
            textTime.setText(input.getTime());
        }

        if (input.getElements() != null) {
            textElements.setText(input.getElements());
        }

        if (input.getLookups() != null) {
            textLookups.setText(input.getLookups());
        }

        if (input.getDivision() != null) {
            textDivision.setText(input.getDivision());
        }

        if (input.getUri() != null) {
            textUri.setText(input.getUri());
        }

        if (input.getProbability() != null) {
            textProbability.setText(input.getProbability());
        }

        if (input.getHashFunction() != null) {
            comboHashFunction.setText(input.getHashFunction());
        }

        checkAlwaysPassRow.setSelection(false);
        checkTransactional.setSelection(false);
        textUniqueField.setEnabled(false);

        if (input.isAlwaysPassRow()) {
            checkAlwaysPassRow.setSelection(true);
            textUniqueField.setEnabled(true);
        }

        if (input.isTransactional()) {
            checkTransactional.setSelection(true);
        }

        if (input.getUniqueFieldName() != null) {
            textUniqueField.setText(input.getUniqueFieldName());
        }

        String filter = input.getFilter();

        if (filter == null || filter.length() < 1) {
            filter = FilterType.BLOOM.toString();
        }

        setFilterType(filter);
    }

    private void setFilterType(String filter)
    {
        if (FilterType.BLOOM.toString().equals(filter)) {
            textProbability.setEnabled(true);
            textElements.setEnabled(true);
            comboFilterType.select(0);

            return;
        }

        textProbability.setEnabled(false);
        textElements.setEnabled(false);
        comboFilterType.select(1);
    }

    private void cancel()
    {
        stepname = null;
        input.setChanged(changed);

        dispose();
    }

    private void ok()
    {
        stepname = wStepname.getText();

        input.setUri(textUri.getText());
        input.setHash(textHash.getText());
        input.setTime(textTime.getText());
        input.setLookups(textLookups.getText());
        input.setFilter(comboFilterType.getText());
        input.setDivision(textDivision.getText());
        input.setElements(textElements.getText());
        input.setProbability(textProbability.getText());
        input.setHashFunction(comboHashFunction.getText());
        input.setUniqueFieldName(textUniqueField.getText());
        input.setAlwaysPassRow(checkAlwaysPassRow.getSelection());
        input.setTransactional(checkTransactional.getSelection());

        dispose();
    }
}
