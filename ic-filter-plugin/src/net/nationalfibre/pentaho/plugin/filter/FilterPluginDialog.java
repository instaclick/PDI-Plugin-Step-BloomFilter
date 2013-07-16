package net.nationalfibre.pentaho.plugin.filter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class FilterPluginDialog extends BaseStepDialog implements StepDialogInterface {

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

    public FilterPluginDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
        super(parent, (BaseStepMeta) in, transMeta, sname);

        input = (FilterPluginMeta) in;
    }

    public String open() {
        Shell parent    = getParent();
        Display display = parent.getDisplay();
        shell           = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        props.setLook(shell);
        setShellImage(shell, input);

        ModifyListener lsMod = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                input.setChanged();
            }
        };

        changed = input.hasChanged();

        FormLayout formLayout   = new FormLayout();
        formLayout.marginWidth  = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout(formLayout);
        shell.setText(Messages.getString("FilterPlugin.Shell.Title"));

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText(Messages.getString("FilterPlugin.StepName.Label"));
        props.setLook(wlStepname);

        fdlStepname         = new FormData();
        fdlStepname.left    = new FormAttachment(0, 0);
        fdlStepname.right   = new FormAttachment(middle, -margin);
        fdlStepname.top     = new FormAttachment(0, margin);

        wlStepname.setLayoutData(fdlStepname);
        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(lsMod);

        fdStepname       = new FormData();
        fdStepname.left  = new FormAttachment(middle, 0);
        fdStepname.top   = new FormAttachment(0, margin);
        fdStepname.right = new FormAttachment(100, 0);

        wStepname.setLayoutData(fdStepname);

        // hash line
        labelHash = new Label(shell, SWT.RIGHT);
        labelHash.setText(Messages.getString("FilterPlugin.ValueHash.Label"));
        props.setLook(labelHash);

        formHashLabel       = new FormData();
        formHashLabel.left  = new FormAttachment(0, 0);
        formHashLabel.right = new FormAttachment(middle, -margin);
        formHashLabel.top   = new FormAttachment(wStepname, margin);

        labelHash.setLayoutData(formHashLabel);

        textHash = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textHash);
        textHash.addModifyListener(lsMod);

        formHashText        = new FormData();
        formHashText.left   = new FormAttachment(middle, 0);
        formHashText.right  = new FormAttachment(100, 0);
        formHashText.top    = new FormAttachment(wStepname, margin);

        textHash.setLayoutData(formHashText);

        // time line
        labelTime = new Label(shell, SWT.RIGHT);
        labelTime.setText(Messages.getString("FilterPlugin.ValueTime.Label"));
        props.setLook(labelTime);

        formTimeLabel       = new FormData();
        formTimeLabel.left  = new FormAttachment(0, 0);
        formTimeLabel.right = new FormAttachment(middle, -margin);
        formTimeLabel.top   = new FormAttachment(textHash, margin);

        labelTime.setLayoutData(formTimeLabel);
        textTime = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textTime);
        textTime.addModifyListener(lsMod);

        formTimeText        = new FormData();
        formTimeText.left   = new FormAttachment(middle, 0);
        formTimeText.right  = new FormAttachment(100, 0);
        formTimeText.top    = new FormAttachment(textHash, margin);
        textTime.setLayoutData(formTimeText);

        // FilterSize line
        labelElements = new Label(shell, SWT.RIGHT);
        labelElements.setText(Messages.getString("FilterPlugin.ValueElements.Label"));
        props.setLook(labelElements);

        formElementsLabel       = new FormData();
        formElementsLabel.left  = new FormAttachment(0, 0);
        formElementsLabel.right = new FormAttachment(middle, -margin);
        formElementsLabel.top   = new FormAttachment(textTime, margin);

        labelElements.setLayoutData(formElementsLabel);

        textElements = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textElements);
        textElements.addModifyListener(lsMod);

        formElementsText        = new FormData();
        formElementsText.left   = new FormAttachment(middle, 0);
        formElementsText.right  = new FormAttachment(100, 0);
        formElementsText.top    = new FormAttachment(textTime, margin);

        textElements.setLayoutData(formElementsText);

        // FilterProbability line
        labelProbability = new Label(shell, SWT.RIGHT);

        labelProbability.setText(Messages.getString("FilterPlugin.ValueProbability.Label"));
        props.setLook(labelProbability);

        formProbabilityLabel        = new FormData();
        formProbabilityLabel.left   = new FormAttachment(0, 0);
        formProbabilityLabel.right  = new FormAttachment(middle, -margin);
        formProbabilityLabel.top    = new FormAttachment(textElements, margin);

        labelProbability.setLayoutData(formProbabilityLabel);

        textProbability = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textProbability);
        textProbability.addModifyListener(lsMod);

        formProbabilityText         = new FormData();
        formProbabilityText.left    = new FormAttachment(middle, 0);
        formProbabilityText.right   = new FormAttachment(100, 0);
        formProbabilityText.top     = new FormAttachment(textElements, margin);

        textProbability.setLayoutData(formProbabilityText);

        // FilterDirectory line
        labelUri = new Label(shell, SWT.RIGHT);

        labelUri.setText(Messages.getString("FilterPlugin.ValueUri.Label"));
        props.setLook(labelUri);

        formUriLabel        = new FormData();
        formUriLabel.left   = new FormAttachment(0, 0);
        formUriLabel.right  = new FormAttachment(middle, -margin);
        formUriLabel.top    = new FormAttachment(textProbability, margin);

        labelUri.setLayoutData(formUriLabel);

        textUri = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textUri);
        textUri.addModifyListener(lsMod);

        formUriText         = new FormData();
        formUriText.left    = new FormAttachment(middle, 0);
        formUriText.right   = new FormAttachment(100, 0);
        formUriText.top     = new FormAttachment(textProbability, margin);

        textUri.setLayoutData(formUriText);

        // FilterLookups line
        labelLookups = new Label(shell, SWT.RIGHT);

        labelLookups.setText(Messages.getString("FilterPlugin.ValueLookups.Label"));
        props.setLook(labelLookups);

        formLookupsLabel        = new FormData();
        formLookupsLabel.left   = new FormAttachment(0, 0);
        formLookupsLabel.right  = new FormAttachment(middle, -margin);
        formLookupsLabel.top    = new FormAttachment(textUri, margin);

        labelLookups.setLayoutData(formLookupsLabel);

        textLookups = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textLookups);
        textLookups.addModifyListener(lsMod);

        formLookupsText         = new FormData();
        formLookupsText.left    = new FormAttachment(middle, 0);
        formLookupsText.right   = new FormAttachment(100, 0);
        formLookupsText.top     = new FormAttachment(textUri, margin);

        textLookups.setLayoutData(formLookupsText);

        // FilterDivision line
        labelDivision = new Label(shell, SWT.RIGHT);

        labelDivision.setText(Messages.getString("FilterPlugin.ValueDivision.Label"));
        props.setLook(labelDivision);

        formDivisionLabel       = new FormData();
        formDivisionLabel.left  = new FormAttachment(0, 0);
        formDivisionLabel.right = new FormAttachment(middle, -margin);
        formDivisionLabel.top   = new FormAttachment(textLookups, margin);

        labelDivision.setLayoutData(formDivisionLabel);

        textDivision = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);

        props.setLook(textDivision);
        textDivision.addModifyListener(lsMod);

        formDivisionText        = new FormData();
        formDivisionText.left   = new FormAttachment(middle, 0);
        formDivisionText.right  = new FormAttachment(100, 0);
        formDivisionText.top    = new FormAttachment(textLookups, margin);

        textDivision.setLayoutData(formDivisionText);

        // Some buttons
        wOK     = new Button(shell, SWT.PUSH);
        wCancel = new Button(shell, SWT.PUSH);

        wOK.setText(Messages.getString("System.Button.OK"));
        wCancel.setText(Messages.getString("System.Button.Cancel"));

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
    public void getData() {
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
    }

    private void cancel() {
        stepname = null;
        input.setChanged(changed);

        dispose();
    }

    private void ok() {
        stepname = wStepname.getText();

        input.setUri(textUri.getText());
        input.setHash(textHash.getText());
        input.setTime(textTime.getText());
        input.setLookups(textLookups.getText());
        input.setDivision(textDivision.getText());
        input.setElements(textElements.getText());
        input.setProbability(textProbability.getText());

        dispose();
    }
}
