package com.instaclick.pentaho.plugin.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import com.instaclick.filter.FilterType;
import com.instaclick.filter.HashFunctionType;
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
import static com.instaclick.pentaho.plugin.filter.Messages.getString;
import org.eclipse.swt.widgets.Table;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.eclipse.swt.widgets.TableItem;

/**
 * Pentaho filter plugin dialog
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class FilterPluginDialog extends BaseStepDialog implements StepDialogInterface
{
    private FilterPluginMeta input;

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

    private Label labelFilterFile;
    private Text textFilterFile;
    private FormData formFilterFileLabel;
    private FormData formFilterFileText;

    private Label labelTransactional;
    private Button checkTransactional;
    private FormData formTransactionalLabel;
    private FormData formTransactionalText;

    private Label labelCheckOnly;
    private Button checkCheckOnly;
    private FormData formCheckOnlyLabel;
    private FormData formCheckOnlyText;

    private Label labelFields;
    private TableView tableFields;
    private FormData fromFieldsLabel;
    private FormData formFieldsTable;

    private ColumnInfo[] colinf = new ColumnInfo[] {
        new ColumnInfo(getString("FilterPlugin.Fields.Label"), ColumnInfo.COLUMN_TYPE_TEXT, false),
    };

    private static final String[] hashFunctionTypes = HashFunctionType.getHashFunctionNames();
    private static final List<String> filterTypes = new ArrayList<String>(Arrays.asList(new String[]{
        FilterType.BLOOM.toString(),
        FilterType.MAP.toString(),
        FilterType.SINGLE_BLOOM.toString()
    }));

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

            textFilterFile.setEnabled(false);
            textDivision.setEnabled(true);
            textLookups.setEnabled(true);

            if (FilterType.BLOOM.toString().equals(comboFilterType.getText()) || FilterType.SINGLE_BLOOM.toString().equals(comboFilterType.getText())) {
                textProbability.setEnabled(true);
                textElements.setEnabled(true);

                if (FilterType.SINGLE_BLOOM.toString().equals(comboFilterType.getText())) {
                    textFilterFile.setEnabled(true);
                    textDivision.setEnabled(false);
                    textLookups.setEnabled(false);
                }

                return;
            }

            textProbability.setEnabled(false);
            textElements.setEnabled(false); 
        }
    };

    private final SelectionAdapter checkAlwaysPassRowListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {

            if (checkAlwaysPassRow.getSelection()) {
                textUniqueField.setEnabled(true);

                return;
            }

            textUniqueField.setEnabled(false);
        }
    };

    private final SelectionAdapter checkCheckOnlyListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {

            if (checkCheckOnly.getSelection()) {
                checkTransactional.setEnabled(false);

                return;
            }

            checkTransactional.setEnabled(true);
        }
    };


    public FilterPluginDialog(Shell parent, Object in, TransMeta transMeta, String sname)
    {
        super(parent, (BaseStepMeta) in, transMeta, sname);

        input = (FilterPluginMeta) in;
    }

    @Override
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
        comboFilterType.setItems(filterTypes.toArray(new String[filterTypes.size()]));
        props.setLook(comboFilterType);

        formFilterTypeCombo      = new FormData();
        formFilterTypeCombo.left = new FormAttachment(middle, margin);
        formFilterTypeCombo.top  = new FormAttachment(wStepname, margin);
        formFilterTypeCombo.right= new FormAttachment(100, 0);

        comboFilterType.setLayoutData(formFilterTypeCombo);

        // Hash Function
        labelHashFunction=new Label(shell, SWT.RIGHT);
        labelHashFunction.setText(getString("FilterPlugin.HashFunction.Label"));
        props.setLook(labelHashFunction);

        formHashFunctionLabel       = new FormData();
        formHashFunctionLabel.left  = new FormAttachment(0, 0);
        formHashFunctionLabel.top   = new FormAttachment(comboFilterType, margin);
        formHashFunctionLabel.right = new FormAttachment(middle, 0);

        labelHashFunction.setLayoutData(formHashFunctionLabel);

        comboHashFunction = new CCombo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.READ_ONLY);

        comboHashFunction.setToolTipText(getString("FilterPlugin.HashFunction.Label"));
        comboHashFunction.setItems(hashFunctionTypes);
        comboHashFunction.addModifyListener(modifyListener);
        props.setLook(comboHashFunction);
        
        formHashFunctionCombo      = new FormData();
        formHashFunctionCombo.left = new FormAttachment(middle, margin);
        formHashFunctionCombo.top  = new FormAttachment(comboFilterType, margin);
        formHashFunctionCombo.right= new FormAttachment(100, 0);

        comboHashFunction.setLayoutData(formHashFunctionCombo);

        // Transactional
        labelTransactional = new Label(shell, SWT.RIGHT);
        labelTransactional.setText(getString("FilterPlugin.Transactional.Label"));
        props.setLook(labelTransactional);

        formTransactionalLabel       = new FormData();
        formTransactionalLabel.left  = new FormAttachment(0, 0);
        formTransactionalLabel.right = new FormAttachment(middle, -margin);
        formTransactionalLabel.top   = new FormAttachment(comboHashFunction , margin);

        labelTransactional.setLayoutData(formTransactionalLabel);

        checkTransactional = new Button(shell, SWT.CHECK);
        props.setLook(checkTransactional);
        checkTransactional.addSelectionListener(selectionModifyListener);

        formTransactionalText        = new FormData();
        formTransactionalText.left   = new FormAttachment(middle, 0);
        formTransactionalText.right  = new FormAttachment(100, 0);
        formTransactionalText.top    = new FormAttachment(comboHashFunction, margin);

        checkTransactional.setLayoutData(formTransactionalText);

        // CheckOnly
        labelCheckOnly = new Label(shell, SWT.RIGHT);
        labelCheckOnly.setText(getString("FilterPlugin.CheckOnly.Label"));
        props.setLook(labelCheckOnly);

        formCheckOnlyLabel       = new FormData();
        formCheckOnlyLabel.left  = new FormAttachment(0, 0);
        formCheckOnlyLabel.right = new FormAttachment(middle, -margin);
        formCheckOnlyLabel.top   = new FormAttachment(checkTransactional , margin);

        labelCheckOnly.setLayoutData(formCheckOnlyLabel);

        checkCheckOnly = new Button(shell, SWT.CHECK);
        props.setLook(checkCheckOnly);
        checkCheckOnly.addSelectionListener(checkCheckOnlyListener);
        checkCheckOnly.addSelectionListener(selectionModifyListener);

        formCheckOnlyText        = new FormData();
        formCheckOnlyText.left   = new FormAttachment(middle, 0);
        formCheckOnlyText.right  = new FormAttachment(100, 0);
        formCheckOnlyText.top    = new FormAttachment(checkTransactional, margin);

        checkCheckOnly.setLayoutData(formCheckOnlyText);

        // Always Pass The Row
        labelAlwaysPassRow = new Label(shell, SWT.RIGHT);
        labelAlwaysPassRow.setText(getString("FilterPlugin.AlwaysPassRow.Label"));
        props.setLook(labelAlwaysPassRow);

        formAlwaysPassRowLabel       = new FormData();
        formAlwaysPassRowLabel.left  = new FormAttachment(0, 0);
        formAlwaysPassRowLabel.right = new FormAttachment(middle, -margin);
        formAlwaysPassRowLabel.top   = new FormAttachment(checkCheckOnly, margin);

        labelAlwaysPassRow.setLayoutData(formAlwaysPassRowLabel);

        checkAlwaysPassRow = new Button(shell, SWT.CHECK);
        checkAlwaysPassRow.addSelectionListener(checkAlwaysPassRowListener);
        checkAlwaysPassRow.addSelectionListener(selectionModifyListener);
        props.setLook(checkAlwaysPassRow);

        formAlwaysPassRowText        = new FormData();
        formAlwaysPassRowText.left   = new FormAttachment(middle, 0);
        formAlwaysPassRowText.right  = new FormAttachment(100, 0);
        formAlwaysPassRowText.top    = new FormAttachment(checkCheckOnly, margin);

        checkAlwaysPassRow.setLayoutData(formAlwaysPassRowText);

         // FilterFile line
        labelFilterFile = new Label(shell, SWT.RIGHT);
        labelFilterFile.setText(getString("FilterPlugin.FilterFile.Label"));
        props.setLook(labelFilterFile);

        formFilterFileLabel       = new FormData();
        formFilterFileLabel.left  = new FormAttachment(0, 0);
        formFilterFileLabel.right = new FormAttachment(middle, -margin);
        formFilterFileLabel.top   = new FormAttachment(checkAlwaysPassRow , margin);

        labelFilterFile.setLayoutData(formFilterFileLabel);

        textFilterFile = new Text(shell, SWT.MULTI | SWT.LEFT | SWT.BORDER);

        props.setLook(textFilterFile);
        textFilterFile.addModifyListener(modifyListener);

        formFilterFileText        = new FormData();
        formFilterFileText.left   = new FormAttachment(middle, 0);
        formFilterFileText.right  = new FormAttachment(100, 0);
        formFilterFileText.top    = new FormAttachment(checkAlwaysPassRow, margin);

        textFilterFile.setLayoutData(formFilterFileText);

         // UniqueField line
        labelUniqueField = new Label(shell, SWT.RIGHT);
        labelUniqueField.setText(getString("FilterPlugin.UniqueField.Label"));
        props.setLook(labelUniqueField);

        formUniqueFieldLabel       = new FormData();
        formUniqueFieldLabel.left  = new FormAttachment(0, 0);
        formUniqueFieldLabel.right = new FormAttachment(middle, -margin);
        formUniqueFieldLabel.top   = new FormAttachment(labelFilterFile , margin);

        labelUniqueField.setLayoutData(formUniqueFieldLabel);

        textUniqueField = new Text(shell, SWT.MULTI | SWT.LEFT | SWT.BORDER);

        props.setLook(textUniqueField);
        textUniqueField.addModifyListener(modifyListener);

        formUniqueFieldText        = new FormData();
        formUniqueFieldText.left   = new FormAttachment(middle, 0);
        formUniqueFieldText.right  = new FormAttachment(100, 0);
        formUniqueFieldText.top    = new FormAttachment(labelFilterFile, margin);

        textUniqueField.setLayoutData(formUniqueFieldText);

        // time line
        labelTime = new Label(shell, SWT.RIGHT);
        labelTime.setText(getString("FilterPlugin.Time.Label"));
        props.setLook(labelTime);

        formTimeLabel       = new FormData();
        formTimeLabel.left  = new FormAttachment(0, 0);
        formTimeLabel.right = new FormAttachment(middle, -margin);
        formTimeLabel.top   = new FormAttachment(textUniqueField, margin);

        labelTime.setLayoutData(formTimeLabel);
        textTime = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textTime);
        textTime.addModifyListener(modifyListener);

        formTimeText        = new FormData();
        formTimeText.left   = new FormAttachment(middle, 0);
        formTimeText.right  = new FormAttachment(100, 0);
        formTimeText.top    = new FormAttachment(textUniqueField, margin);
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

        // Fields line
        labelFields = new Label(shell, SWT.RIGHT);

        labelFields.setText(getString("FilterPlugin.Fields.Label"));
        props.setLook(labelFields);

        fromFieldsLabel       = new FormData();
        fromFieldsLabel.left  = new FormAttachment(0, 0);
        fromFieldsLabel.right = new FormAttachment(middle, -margin);
        fromFieldsLabel.top   = new FormAttachment(textDivision, margin);

        labelFields.setLayoutData(fromFieldsLabel);

        tableFields             = new TableView(transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, colinf, 0, modifyListener ,props);
        formFieldsTable         = new FormData();
        formFieldsTable.left    = new FormAttachment(middle, 0);
        formFieldsTable.right   = new FormAttachment(100, 0);
        formFieldsTable.top     = new FormAttachment(textDivision, margin);
        formFieldsTable.bottom  = new FormAttachment(90, -margin);

        tableFields.setLayoutData(formFieldsTable);

        // Some buttons
        wOK     = new Button(shell, SWT.PUSH);
        wCancel = new Button(shell, SWT.PUSH);

        wOK.setText(getString("System.Button.OK"));
        wCancel.setText(getString("System.Button.Cancel"));

        setButtonPositions(new Button[] { wOK, wCancel }, margin, null);

        // Add listeners
        lsCancel = new Listener() {
            @Override
            public void handleEvent(Event e) {
                cancel();
            }
        };
        lsOK = new Listener() {
            @Override
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

        if (input.getSingleFilterFile()!= null) {
            textFilterFile.setText(input.getSingleFilterFile());
        }

        textDivision.setEnabled(true);
        textLookups.setEnabled(true);
        checkAlwaysPassRow.setSelection(false);
        checkTransactional.setSelection(false);
        checkCheckOnly.setSelection(false);
        textUniqueField.setEnabled(false);
        textFilterFile.setEnabled(false);
        checkTransactional.setEnabled(true);

        if (input.isAlwaysPassRow()) {
            checkAlwaysPassRow.setSelection(true);
            textUniqueField.setEnabled(true);
        }

        if (input.isTransactional()) {
            checkTransactional.setSelection(true);
        }

        if (input.isCheckOnly()) {
            checkTransactional.setEnabled(false);
            checkCheckOnly.setSelection(true);
        }

        if (input.getIsUniqueFieldName() != null) {
            textUniqueField.setText(input.getIsUniqueFieldName());
        }

        String filter = input.getFilter();

        if (filter == null || filter.length() < 1) {
            filter = FilterType.BLOOM.toString();
        }

        setFilterType(filter);

        Table table         = tableFields.table;
        String[] fieldNames = input.getUniqueFieldsName();

        if (fieldNames.length > 0) {
            table.removeAll();
        }

        for (int i = 0; i < fieldNames.length; i++) {
            TableItem ti = new TableItem(table, SWT.NONE);
            ti.setText(0, String.valueOf((i + 1)));
            ti.setText(1, (Const.isEmpty(fieldNames[i])) ? "" : fieldNames[i]);
        }

        tableFields.setRowNums();
        tableFields.optWidth(true);

        wStepname.selectAll();
    }

    private void setFilterType(String filter)
    {
        textFilterFile.setEnabled(false);
        textDivision.setEnabled(true);
        textLookups.setEnabled(true);

        int index = filterTypes.indexOf(filter);

        if (index == -1) {
            index = 0;
        }

        comboFilterType.select(index);

        if (FilterType.BLOOM.toString().equals(filter) || FilterType.SINGLE_BLOOM.toString().equals(filter)) {
            textProbability.setEnabled(true);
            textElements.setEnabled(true);

            if (FilterType.SINGLE_BLOOM.toString().equals(filter)) {
                textFilterFile.setEnabled(true);
                textDivision.setEnabled(false);
                textLookups.setEnabled(false);
            }

            return;
        }

        textProbability.setEnabled(false);
        textElements.setEnabled(false);
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
        input.setTime(textTime.getText());
        input.setLookups(textLookups.getText());
        input.setFilter(comboFilterType.getText());
        input.setDivision(textDivision.getText());
        input.setElements(textElements.getText());
        input.setProbability(textProbability.getText());
        input.setHashFunction(comboHashFunction.getText());
        input.setIsUniqueFieldName(textUniqueField.getText());
        input.setSingleFilterFile(textFilterFile.getText());
        input.setAlwaysPassRow(checkAlwaysPassRow.getSelection());
        input.setTransactional(checkTransactional.getSelection());
        input.setCheckOnly(checkCheckOnly.getSelection());

        String[] fieldNames = new String[tableFields.nrNonEmpty()];

        for (int i = 0; i < fieldNames.length; i++) {
            final TableItem item = tableFields.getNonEmpty(i);
            final String name    = item.getText(1);
            fieldNames[i]        = name.replace(",", "").trim();
        }

        if (fieldNames.length == 0) {
            tableFields.setFocus();

            return;
        }

        input.setUniqueFieldsName(fieldNames);

        dispose();
    }
}
