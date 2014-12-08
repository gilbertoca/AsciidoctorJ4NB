package org.netbeans.asciidoc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@MultiViewElement.Registration(
        displayName = "#LBL_Adoc_VISUAL",
        iconBase = "org/netbeans/asciidoc/resources/icon.png",
        mimeType = "text/x-adoc",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "AdocVisual",
        position = 2000
)
@Messages("LBL_Adoc_VISUAL=Visual")
public final class AdocVisualElement extends JPanel implements MultiViewElement {

    private final AdocDataObject obj;
    private final JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;

    public AdocVisualElement(Lookup lkp) {
        obj = lkp.lookup(AdocDataObject.class);
        assert obj != null;
        initComponents();
        convert();
        obj.getPrimaryFile().addFileChangeListener(new FileChangeAdapter(){
            @Override
            public void fileChanged(FileEvent fe) {
                convert();
            }
        });
    }

    private void convert() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProgressHandle h = ProgressHandleFactory.createHandle("Converting...");
                    h.start();
                    Asciidoctor doctor = Asciidoctor.Factory.create(Arrays.asList(
                            "gems/asciidoctor-1.5.2/lib",
                            "gems/coderay-1.1.0/lib",
                            "META-INF/jruby.home/lib/ruby/1.9"));
                    String html = doctor.convert(obj.getPrimaryFile().asText(), getInitialOptions());
                    htmlEditorPane.setText(html);
                    h.finish();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        t.start();
    }

    public Map<String, Object> getInitialOptions() {
        Attributes attrs = AttributesBuilder.attributes().
                showTitle(true)
                .sourceHighlighter("coderay").
                attribute("coderay-css", "style").
                get();
        OptionsBuilder opts = OptionsBuilder.options().safe(
                SafeMode.SAFE).
                backend("html5").
                headerFooter(true).
                attributes(attrs);
        return opts.asMap();
    }

    @Override
    public String getName() {
        return "AdocVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        htmlEditorPane = new javax.swing.JEditorPane();

        htmlEditorPane.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(htmlEditorPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane htmlEditorPane;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        toolbar.setFloatable(false);
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

}
