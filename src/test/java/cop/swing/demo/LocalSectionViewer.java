package cop.swing.demo;

import cop.swing.controls.layouts.SingleColumnLayout;
import cop.swing.controls.layouts.SingleRowLayout;
import cop.swing.controls.sections.SectionViewer;
import cop.swing.providers.BackgroundProvider;
import cop.swing.providers.GroupBackgroundProvider;

import java.awt.Color;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSectionViewer extends SectionViewer<LocalSection> {
    protected LocalSectionViewer() {
        setDraggable(true);
    }

    public void setSpace(int space) {
        if (panel.getLayoutOrganizer() instanceof SingleColumnLayout)
            ((SingleColumnLayout)panel.getLayoutOrganizer()).setSpace(space);
        else if (panel.getLayoutOrganizer() instanceof SingleRowLayout)
            ((SingleRowLayout)panel.getLayoutOrganizer()).setSpace(space);

        updateUI();
    }

    public void setAlignment(int alignment) {
        if (panel.getLayoutOrganizer() instanceof SingleColumnLayout)
            ((SingleColumnLayout)panel.getLayoutOrganizer()).setAlignment(alignment);
        else if (panel.getLayoutOrganizer() instanceof SingleRowLayout)
            ((SingleRowLayout)panel.getLayoutOrganizer()).setAlignment(alignment);
        updateUI();
    }

    // ========== SectionViewer ==========

    @Override
    protected BackgroundProvider getSectionBackgroundProvider() {
        return new GroupBackgroundProvider(Color.lightGray, Color.gray);
    }
}
