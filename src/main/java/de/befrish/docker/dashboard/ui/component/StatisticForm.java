package de.befrish.docker.dashboard.ui.component;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import de.befrish.docker.dashboard.domain.ContainerStatistic;

import java.text.DecimalFormat;
import java.util.Optional;

public class StatisticForm extends FormLayout
        implements HasValue<HasValue.ValueChangeEvent<ContainerStatistic>, ContainerStatistic> {

    private final DecimalFormat PERCENTAGE_DECIMAL_FORMAT = new DecimalFormat("0.##%");

    private ContainerStatistic containerStatistic;

    private final TextField cpuField;

    public StatisticForm() {
        cpuField = new TextField("CPU");
        cpuField.setReadOnly(true);
        add(cpuField);
    }

    @Override
    public ContainerStatistic getValue() {
        return containerStatistic;
    }

    @Override
    public void setValue(final ContainerStatistic value) {
        this.containerStatistic = value;
        updateFields(value);
    }

    private void updateFields(final ContainerStatistic containerStatistic) {
        Optional.ofNullable(containerStatistic).ifPresentOrElse(
                containerStatistic1 -> {
                    cpuField.setValue(PERCENTAGE_DECIMAL_FORMAT.format(containerStatistic1.getCpuInPercent()));
                    // TODO implement
                },
                () -> {
                    cpuField.clear();
                    // TODO implement
                }
        );
    }

    @Override
    public Registration addValueChangeListener(
            final ValueChangeListener<? super ValueChangeEvent<ContainerStatistic>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public void setRequiredIndicatorVisible(final boolean requiredIndicatorVisible) {
        throw new UnsupportedOperationException();
    }

}
