/*
 * RSSReaderView.java
 */

package rssreader;

import com.sun.syndication.feed.synd.SyndEntry;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 * The application's main frame.
 */
public class RSSReaderView extends FrameView {

    Reader myReader = new Reader();
    MyListSelectionListener listener = new MyListSelectionListener();
    MyChannelListSelectionListener chListener = new MyChannelListSelectionListener();
    //http://feeds.bbci.co.uk/news/rss.xml
    Channel bbc = new Channel("BBC","http://localhost/rss.xml");
    public RSSReaderView(SingleFrameApplication app) {
        super(app);
        initComponents();
        this.getFrame().setResizable(false);
        jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        panel = new ImagePanel(new ImageIcon("E:/Programming/Java/RSSReader/src/rssreader/resources/bg.gif").getImage());
        mainPanel.add(panel);

        textarea_description.setLineWrap(true);
        channelModel.addElement(bbc.channelName);
        addedChannel.add(bbc);
        channelList.addListSelectionListener(chListener);
        setFeeds(bbc.url);
        openBrowser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OpenURI openuri = new OpenURI(browserURI);
            }
        });
        
        //expandLabel.add
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = RSSReaderApp.getApplication().getMainFrame();
            aboutBox = new RSSReaderAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        RSSReaderApp.getApplication().show(aboutBox);
    }

    @Action
    public void showConnectionDialog(){
        if(connectionDialog == null){
            JFrame mainFrame = RSSReaderApp.getApplication().getMainFrame();
            connectionDialog = new ConnectionDialog(mainFrame,true);
            connectionDialog.setLocationRelativeTo(mainFrame);
            connectionDialog.setResizable(false);
            RSSReaderApp.getApplication().show(connectionDialog);
            if(connectionDialog.getReturnStatus() == 1){
               myReader.proxy = connectionDialog.getProxy();
               myReader.port = connectionDialog.getPort();
               myReader.username = connectionDialog.getUsername();
               myReader.password = connectionDialog.getPassword();
            }
        }
    }

    @Action
    public void add_subscription(){
        if(addSubscriptionDialog == null){
            JFrame mainFrame = RSSReaderApp.getApplication().getMainFrame();
            addSubscriptionDialog = new AddSubscription(mainFrame, true);
            addSubscriptionDialog.setLocationRelativeTo(mainFrame);
            addSubscriptionDialog.setTitle("Add Subscription");
            addSubscriptionDialog.setResizable(false);
            RSSReaderApp.getApplication().show(addSubscriptionDialog);
            if(addSubscriptionDialog.getReturnStatus() == 1){
                ch = new Channel(addSubscriptionDialog.getName(),addSubscriptionDialog.getURL());
                channelModel.addElement(ch.channelName);
                addedChannel.add(ch);
                channelList.setModel(channelModel);
            }
        }
    }

    @Action
    public void remove_subscription(){
        if(removeSubscriptionDialog == null){
            JFrame mainFrame = RSSReaderApp.getApplication().getMainFrame();
            removeSubscriptionDialog = new RemoveSubscription(mainFrame, true);
            removeSubscriptionDialog.setChannelList(addedChannel);
            removeSubscriptionDialog.setLocationRelativeTo(mainFrame);
            removeSubscriptionDialog.setTitle("Remove Subscription");
            removeSubscriptionDialog.setResizable(false);
            RSSReaderApp.getApplication().show(removeSubscriptionDialog);
            if(removeSubscriptionDialog.getReturnStatus() == 1){
                removed = removeSubscriptionDialog.getRemoved();
                channelModel.removeElement(removed.channelName);
                addedChannel.remove(removed);
            }
        }
    }

    public void setFeeds(URL channelUrl){
        //Reading feeds and displaying it in the GUI
        myReader.read(channelUrl);
        for (Iterator i = myReader.feed.getEntries().iterator(); i.hasNext();) {
		SyndEntry entry = (SyndEntry) i.next();
                model.addElement(entry.getTitle());
         }
        list_items.addListSelectionListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        label_title = new javax.swing.JLabel();
        textfiled_title = new javax.swing.JTextField();
        label_description = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textarea_description = new javax.swing.JTextArea();
        label_date = new javax.swing.JLabel();
        textfield_date = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_items = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        channelList = new javax.swing.JList();
        label_channel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        openBrowser = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        optionMenu = new javax.swing.JMenu();
        connectionMenuItem = new javax.swing.JMenuItem();
        addSubscription = new javax.swing.JMenuItem();
        removeSubscription = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(rssreader.RSSReaderApp.class).getContext().getResourceMap(RSSReaderView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setForeground(resourceMap.getColor("mainPanel.foreground")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(780, 475));

        label_title.setFont(resourceMap.getFont("label_title.font")); // NOI18N
        label_title.setForeground(resourceMap.getColor("label_title.foreground")); // NOI18N
        label_title.setText(resourceMap.getString("label_title.text")); // NOI18N
        label_title.setName("label_title"); // NOI18N

        textfiled_title.setBackground(resourceMap.getColor("text_title.background")); // NOI18N
        textfiled_title.setEditable(false);
        textfiled_title.setFont(resourceMap.getFont("text_title.font")); // NOI18N
        textfiled_title.setText(resourceMap.getString("text_title.text")); // NOI18N
        textfiled_title.setName("text_title"); // NOI18N

        label_description.setFont(resourceMap.getFont("label_description.font")); // NOI18N
        label_description.setForeground(resourceMap.getColor("label_description.foreground")); // NOI18N
        label_description.setText(resourceMap.getString("label_description.text")); // NOI18N
        label_description.setName("label_description"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        textarea_description.setBackground(resourceMap.getColor("textarea_description.background")); // NOI18N
        textarea_description.setColumns(20);
        textarea_description.setEditable(false);
        textarea_description.setFont(resourceMap.getFont("textarea_description.font")); // NOI18N
        textarea_description.setLineWrap(true);
        textarea_description.setRows(5);
        textarea_description.setName("textarea_description"); // NOI18N
        jScrollPane1.setViewportView(textarea_description);

        label_date.setFont(resourceMap.getFont("label_date.font")); // NOI18N
        label_date.setForeground(resourceMap.getColor("label_date.foreground")); // NOI18N
        label_date.setText(resourceMap.getString("label_date.text")); // NOI18N
        label_date.setName("label_date"); // NOI18N

        textfield_date.setBackground(resourceMap.getColor("text_date.background")); // NOI18N
        textfield_date.setEditable(false);
        textfield_date.setFont(resourceMap.getFont("text_date.font")); // NOI18N
        textfield_date.setText(resourceMap.getString("text_date.text")); // NOI18N
        textfield_date.setName("text_date"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        list_items.setBackground(resourceMap.getColor("list_items.background")); // NOI18N
        list_items.setFont(resourceMap.getFont("list_items.font")); // NOI18N
        list_items.setModel(model);
        list_items.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_items.setName("list_items"); // NOI18N
        jScrollPane2.setViewportView(list_items);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        channelList.setBackground(resourceMap.getColor("channelList.background")); // NOI18N
        channelList.setFont(resourceMap.getFont("channelList.font")); // NOI18N
        channelList.setModel(channelModel);
        channelList.setName("channelList"); // NOI18N
        jScrollPane3.setViewportView(channelList);

        label_channel.setFont(resourceMap.getFont("font")); // NOI18N
        label_channel.setForeground(resourceMap.getColor("label_channel.foreground")); // NOI18N
        label_channel.setText(resourceMap.getString("text")); // NOI18N
        label_channel.setName(""); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        openBrowser.setForeground(resourceMap.getColor("openBrowser.foreground")); // NOI18N
        openBrowser.setIcon(resourceMap.getIcon("openBrowser.icon")); // NOI18N
        openBrowser.setText(resourceMap.getString("openBrowser.text")); // NOI18N
        openBrowser.setToolTipText(resourceMap.getString("openBrowser.toolTipText")); // NOI18N
        openBrowser.setName("openBrowser"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label_date)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(label_title, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_description, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                                    .addComponent(textfiled_title, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(openBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(textfield_date, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label_channel)))
                .addGap(104, 104, 104))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(label_channel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textfiled_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_title, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_description, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(openBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textfield_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(483, 483, 483))
        );

        menuBar.setBackground(resourceMap.getColor("menuBar.background")); // NOI18N
        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(rssreader.RSSReaderApp.class).getContext().getActionMap(RSSReaderView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        optionMenu.setText(resourceMap.getString("optionMenu.text")); // NOI18N
        optionMenu.setName("optionMenu"); // NOI18N

        connectionMenuItem.setAction(actionMap.get("showConnectionDialog")); // NOI18N
        connectionMenuItem.setText(resourceMap.getString("connectionMenuItem.text")); // NOI18N
        connectionMenuItem.setName("connectionMenuItem"); // NOI18N
        optionMenu.add(connectionMenuItem);

        addSubscription.setAction(actionMap.get("add_subscription")); // NOI18N
        addSubscription.setText(resourceMap.getString("addSubscription.text")); // NOI18N
        addSubscription.setName("addSubscription"); // NOI18N
        optionMenu.add(addSubscription);

        removeSubscription.setAction(actionMap.get("remove_subscription")); // NOI18N
        removeSubscription.setText(resourceMap.getString("removeSubscription.text")); // NOI18N
        removeSubscription.setName("removeSubscription"); // NOI18N
        optionMenu.add(removeSubscription);

        menuBar.add(optionMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 776, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(696, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addSubscription;
    private javax.swing.JList channelList;
    private javax.swing.JMenuItem connectionMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel label_channel;
    private javax.swing.JLabel label_date;
    private javax.swing.JLabel label_description;
    private javax.swing.JLabel label_title;
    private javax.swing.JList list_items;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton openBrowser;
    private javax.swing.JMenu optionMenu;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem removeSubscription;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextArea textarea_description;
    private javax.swing.JTextField textfield_date;
    private javax.swing.JTextField textfiled_title;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private Channel ch,removed;
    private List<Channel> addedChannel = new ArrayList<Channel>();
    private ConnectionDialog connectionDialog;
    private AddSubscription addSubscriptionDialog;
    private RemoveSubscription removeSubscriptionDialog;
    private DefaultListModel model = new DefaultListModel();
    private DefaultListModel channelModel = new DefaultListModel();
    private String browserURI = "";
    ImagePanel panel ;

    class MyListSelectionListener implements ListSelectionListener {
    // This method is called each time the user changes the set of selected items
        public void valueChanged(ListSelectionEvent evt) {
            // When the user release the mouse button and completes the selection,
            // getValueIsAdjusting() becomes false
            if (!evt.getValueIsAdjusting()) {
                JList list = (JList)evt.getSource();

                // Get all selected items
                Object selected = list.getSelectedValue();
                for(Iterator i = myReader.feed.getEntries().iterator();i.hasNext();){
                SyndEntry entry = (SyndEntry) i.next();
                if(entry.getTitle().equals(selected)){
                    textfiled_title.setText(entry.getTitle());
                    textarea_description.setText(entry.getDescription().getValue());
                    textfield_date.setText(entry.getPublishedDate().toString());
                    browserURI = entry.getLink();
                    }
                }
            }
        }
    }

    public class MyChannelListSelectionListener implements ListSelectionListener{
        //Changes Channel as well as the value of URL
        public void valueChanged(ListSelectionEvent e) {
             if (!e.getValueIsAdjusting()) {
                JList list = (JList)e.getSource();

                // Get all selected items
                Object selected = list.getSelectedValue();
                model.removeAllElements();
                Iterator i = addedChannel.iterator();
                while(i.hasNext()){
                    Channel temp = (Channel) i.next();
                    if(temp.channelName.equals(selected)){
                        setFeeds(temp.url);
                    }
                }
            }
        }

    }

  class ImagePanel extends JPanel {

  private Image img;

  public ImagePanel(String img) {
    this(new ImageIcon(img).getImage());
  }

  public ImagePanel(Image img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }

  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
     }

    }
}
