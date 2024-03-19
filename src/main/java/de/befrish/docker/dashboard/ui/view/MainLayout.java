package de.befrish.docker.dashboard.ui.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.internal.HasUrlParameterFormat;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.befrish.docker.dashboard.service.SettingsResolver;
import de.befrish.docker.dashboard.ui.service.NameUrlMapper;
import lombok.NonNull;

@Theme(variant = Lumo.DARK)
@Push
public class MainLayout extends AppLayout implements AppShellConfigurator {

    public MainLayout(
            @NonNull final SettingsResolver settingsResolver,
            @NonNull final NameUrlMapper nameUrlMapper) {
        createHeader();
        createDrawer(settingsResolver, nameUrlMapper);
        setPrimarySection(Section.NAVBAR);
    }

    private void createHeader() {
        final var toggle = new DrawerToggle();

        final var title = new H1("Docker Dashboard");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title);
    }

    private void createDrawer(
            final SettingsResolver settingsResolver,
            final NameUrlMapper nameUrlMapper) {

        final var sideNav = createSideNav(settingsResolver, nameUrlMapper);

        final var scroller = new Scroller(sideNav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(sideNav);
    }

    private static SideNav createSideNav(
            final SettingsResolver settingsResolver,
            final NameUrlMapper nameUrlMapper) {

        final var sideNav = new SideNav();
        sideNav.setLabel("Projects");
        settingsResolver.loadSetttings().ifPresent(settings -> sideNav.addItem(settings.getProjects().stream()
                .map(project -> new SideNavItem(
                        project.getName(),
                        ProjectView.class,
                        new RouteParameters(
                                HasUrlParameterFormat.PARAMETER_NAME,
                                nameUrlMapper.mapToUrlParameter(project.getName())),
                        VaadinIcon.DASHBOARD.create()))
                .toArray(SideNavItem[]::new)
        ));
        return sideNav;
    }
}
