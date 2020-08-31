package com.mariocodehouse.tupless.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class InputDialog extends Dialog {

	protected Object result;
	protected Shell shlNomeNoTupless;
	private Text txtDigiteSeuNome;
	private String title;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public InputDialog(String title, Shell parent, int style) {
		super(parent, style);
		this.title = title;
		setText(title);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlNomeNoTupless.open();
		shlNomeNoTupless.layout();
		Display display = getParent().getDisplay();
		while (!shlNomeNoTupless.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlNomeNoTupless = new Shell(getParent(), getStyle());
		shlNomeNoTupless.setSize(200, 87);
		shlNomeNoTupless.setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
		shlNomeNoTupless.setText(title);
		shlNomeNoTupless.setLayout(new GridLayout(1, false));

		txtDigiteSeuNome = new Text(shlNomeNoTupless, SWT.BORDER);
		txtDigiteSeuNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDigiteSeuNome.setFont(SWTResourceManager.getFont("Fira Code", 14, SWT.NORMAL));

		Button btnOk = new Button(shlNomeNoTupless, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String name = txtDigiteSeuNome.getText();
				if (!name.isEmpty()) {
					result = name;
					shlNomeNoTupless.dispose();
				}
			}
		});
		btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnOk.setText("OK");

	}

}
