package com.mariocodehouse.tupless.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class TuplessGUI {

	protected Shell shell;
	private Text txtDigiteSuaMensagem;
	private List list;
	private Composite composite;

	private TuplessGUIListener tuplessListener;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TuplessGUI window = new TuplessGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display.setAppName("Tupless");
		Display.setAppVersion("0.0.1");
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		openInitialDialog();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void openInfoDialog(String title, java.util.List<String> content) {
		shell.setEnabled(false);
		new InfoDialog(title, content, shell, SWT.TITLE).open();
		shell.setEnabled(true);
	}

	public void addMessageToMainList(String message) {
		list.add(message);
	}

	public void cleanMessageList() {
		list.removeAll();
	}

	public void setListener(TuplessGUIListener listener) {
		this.tuplessListener = listener;
	}

	public void runAsync(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.TITLE | SWT.MODELESS);
		shell.setSize(450, 300);
		shell.setLocation(200, 200);
		shell.setText("Tupless Chat");
		shell.setLayout(new GridLayout(1, false));

		composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_FOREGROUND));
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 232;
		composite.setLayoutData(gd_composite);

		list = new List(composite, SWT.BORDER);
		list.setFont(SWTResourceManager.getFont("Fira Code", 16, SWT.NORMAL));

		txtDigiteSuaMensagem = new Text(shell, SWT.BORDER);
		txtDigiteSuaMensagem.setFont(SWTResourceManager.getFont("Fira Code", 16, SWT.NORMAL));
		txtDigiteSuaMensagem.setText("Digite sua mensagem");
		txtDigiteSuaMensagem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String msg = txtDigiteSuaMensagem.getText();
				if (e.keyCode == SWT.CR && !msg.isEmpty()) {
					if (tuplessListener != null)
						tuplessListener.onMessageType(msg);
					list.add("Eu: " + msg);
					txtDigiteSuaMensagem.setText("");
				}
			}
		});
		txtDigiteSuaMensagem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addMenuToShell();

	}

	protected void openInitialDialog() {
		String name = openInputDialog("Apelido Tupless");
		GUIState.getInstance().setUserName(name);
		if (tuplessListener != null)
			tuplessListener.onCreateUserInput(name);

	}

	private String openInputDialog(String title) {
		shell.setEnabled(false);
		String name = (String) new InputDialog(title, shell, SWT.TITLE).open();
		shell.setEnabled(true);
		return name;
	}

	private void addMenuToShell() {
		Menu menuBar = new Menu(shell, SWT.BAR);

		Menu roomsMenu = new Menu(menuBar);
		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		fileItem.setText("Salas");
		fileItem.setMenu(roomsMenu);

		MenuItem createRoomItem = new MenuItem(roomsMenu, SWT.NONE);
		createRoomItem.setText("Criar Sala");
		createRoomItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String name = openInputDialog("Criar::Nome da sala");
				if (tuplessListener != null)
					tuplessListener.onCreateRoomInput(name);
			}
		});

		MenuItem listAllRoomsItem = new MenuItem(roomsMenu, SWT.NONE);
		listAllRoomsItem.setText("Listar salas");
		listAllRoomsItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (tuplessListener != null)
					tuplessListener.onListAllRooms();
			}
		});

		MenuItem joinChatItem = new MenuItem(roomsMenu, SWT.NONE);
		joinChatItem.setText("Conversar");
		joinChatItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String name = openInputDialog("Conversar com um grupo");
				GUIState.getInstance().setCurrentReceiver(name);
				GUIState.getInstance().setCurrentTarget("room");
				if (tuplessListener != null)
					tuplessListener.onJoinRoom(name);
			}
		});

		Menu usersMenu = new Menu(menuBar);
		MenuItem usersItem = new MenuItem(menuBar, SWT.CASCADE);
		usersItem.setText("Usuários");
		usersItem.setMenu(usersMenu);

		MenuItem listUsersItem = new MenuItem(usersMenu, SWT.NONE);
		listUsersItem.setText("Listar Usuários");
		listUsersItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String name = openInputDialog("Listar usuários da sala");
				if (tuplessListener != null)
					tuplessListener.onListAllUsers(name);
			}
		});

		MenuItem chatUserItem = new MenuItem(usersMenu, SWT.NONE);
		chatUserItem.setText("Conversar");
		chatUserItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String name = openInputDialog("Conversar com um amigo");
				GUIState.getInstance().setCurrentReceiver(name);
				GUIState.getInstance().setCurrentTarget("private");
				if (tuplessListener != null)
					tuplessListener.onChatWithUser(name);
			}
		});

		shell.setMenuBar(menuBar);
	}
}
