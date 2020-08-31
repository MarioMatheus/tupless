package com.mariocodehouse.tupless.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class InfoDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String title;
	private java.util.List<String> content;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public InfoDialog(String title, java.util.List<String> content, Shell parent, int style) {
		super(parent, style);
		setText(title);
		this.title = title;
		this.content = content;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), getStyle() | SWT.CLOSE);
		shell.setSize(450, 300);
		shell.setText(title);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		List list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		list.setFont(SWTResourceManager.getFont("Fira Code", 14, SWT.NORMAL));

		for (String element : content) {
			list.add(element);
		}

	}

}
