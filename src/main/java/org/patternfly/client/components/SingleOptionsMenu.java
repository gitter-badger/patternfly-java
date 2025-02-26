package org.patternfly.client.components;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import elemental2.dom.Element;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import org.jboss.gwt.elemento.core.By;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.builder.HtmlContent;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.patternfly.client.core.Disable;
import org.patternfly.client.core.HasValue;
import org.patternfly.client.core.SelectHandler;
import org.patternfly.client.resources.Constants;

import static org.jboss.gwt.elemento.core.Elements.button;
import static org.jboss.gwt.elemento.core.Elements.*;
import static org.jboss.gwt.elemento.core.EventType.click;
import static org.patternfly.client.resources.CSS.component;
import static org.patternfly.client.resources.CSS.fas;
import static org.patternfly.client.resources.CSS.modifier;
import static org.patternfly.client.resources.Constants.label;
import static org.patternfly.client.resources.Constants.toggle;
import static org.patternfly.client.resources.Constants.*;
import static org.patternfly.client.resources.Dataset.singleOptionsMenuItem;

/**
 * PatternFly options menu component.
 *
 * @see <a href= "https://www.patternfly.org/v4/documentation/core/components/optionsmenu">https://www.patternfly.org/v4/documentation/core/components/optionsmenu</a>
 */
public class SingleOptionsMenu<T> extends BaseComponent<HTMLDivElement, SingleOptionsMenu<T>>
        implements HtmlContent<HTMLDivElement, SingleOptionsMenu<T>>, HasValue<T>, Disable<SingleOptionsMenu<T>> {

    // ------------------------------------------------------ factory methods

    public static <T> SingleOptionsMenu<T> text(String text) {
        return new SingleOptionsMenu<>(text, null, false);
    }

    public static <T> SingleOptionsMenu<T> icon(Icon icon) {
        return new SingleOptionsMenu<>(null, icon, false);
    }

    public static <T> SingleOptionsMenu<T> plain(String text) {
        return new SingleOptionsMenu<>(text, null, true);
    }

    // ------------------------------------------------------ options menu instance

    private final CollapseExpandHandler ceh;
    private final ItemDisplay<HTMLButtonElement, T> itemDisplay;
    private T value;
    private SelectHandler<T> onSelect;
    private boolean collapseOnSelect;

    private final HTMLButtonElement button;
    private final HTMLElement plain;
    private final HTMLElement text;
    private final HTMLElement menu;

    SingleOptionsMenu(String text, Icon icon, boolean plain) {
        super(div().css(component(optionsMenu)).element(), "OptionsMenu");
        this.ceh = new CollapseExpandHandler();
        this.itemDisplay = new ItemDisplay<>();
        this.collapseOnSelect = false;

        String buttonId = Elements.uniqueId(optionsMenu, Constants.button);
        HtmlContentBuilder<HTMLButtonElement> buttonBuilder = button()
                .id(buttonId)
                .aria(expanded, false_)
                .aria(hasPopup, listbox)
                .on(click, e -> ceh.expand(element(), buttonElement(), menuElement()));

        HTMLElement trigger;
        if (icon != null) {
            this.plain = null;
            this.text = null;
            this.button = buttonBuilder.css(component(optionsMenu, toggle), modifier(Constants.plain))
                    .add(icon.aria(hidden, true_)).element();
            trigger = button;

        } else { // text != null
            if (plain) {
                this.plain = div().css(component(optionsMenu, toggle), modifier(Constants.plain),
                        modifier(Constants.text))
                        .add(this.text = span().css(component(optionsMenu, toggle, Constants.text))
                                .textContent(text).element())
                        .add(button = buttonBuilder.css(component(optionsMenu, toggle, Constants.button))
                                .aria(label, text)
                                .add(i().css(fas(caretDown)).aria(hidden, true_)).element()).element();
                trigger = this.plain;

            } else {
                this.plain = null;
                this.button = buttonBuilder.css(component(optionsMenu, toggle))
                        .aria(label, text)
                        .add(this.text = span().css(component(optionsMenu, toggle, Constants.text))
                                .textContent(text).element())
                        .add(i().css(fas(caretDown), component(optionsMenu, toggle, Constants.icon))
                                .aria(hidden, true_)).element();
                trigger = button;
            }
        }

        add(trigger);
        add(menu = ul().css(component(optionsMenu, Constants.menu))
                .hidden(true)
                .aria(labelledBy, buttonId)
                .attr(role, Constants.menu).element());
    }

    @Override
    public SingleOptionsMenu<T> that() {
        return this;
    }

    private HTMLElement buttonElement() {
        return button;
    }

    private HTMLElement menuElement() {
        return menu;
    }

    HTMLElement textElement() {
        return text;
    }

    // ------------------------------------------------------ public API

    public SingleOptionsMenu<T> add(Iterable<T> items) {
        for (T item : items) {
            add(item);
        }
        return this;
    }

    public SingleOptionsMenu<T> add(T[] items) {
        for (T item : items) {
            add(item);
        }
        return this;
    }

    public SingleOptionsMenu<T> add(T item) {
        String itemId = itemDisplay.itemId(item);
        HtmlContentBuilder<HTMLButtonElement> button = button()
                .css(component(optionsMenu, Constants.menu, Constants.item))
                .attr(tabindex, _1)
                .data(singleOptionsMenuItem, itemId)
                .on(click, e -> {
                    if (collapseOnSelect) {
                        ceh.collapse(element(), buttonElement(), menuElement());
                    }
                    select(item);
                });
        itemDisplay.display.accept(button, item);
        menu.appendChild(li().attr(role, presentation)
                .add(button).element());
        return this;
    }

    public SingleOptionsMenu<T> identifier(Function<T, String> identifier) {
        itemDisplay.identifier = identifier;
        return this;
    }

    public SingleOptionsMenu<T> display(BiConsumer<HtmlContentBuilder<HTMLButtonElement>, T> display) {
        itemDisplay.display = display;
        return this;
    }

    public SingleOptionsMenu<T> select(T item) {
        value = item;
        String itemId = itemDisplay.itemId(item);
        for (HTMLElement e : findAll(menu, By.data(singleOptionsMenuItem))) {
            Element icon = find(e, By.selector(".fas.fa-check"));
            if (itemId.equals(e.dataset.get(singleOptionsMenuItem))) {
                if (icon == null) {
                    e.appendChild(Components.icon(fas(check))
                            .css(component(optionsMenu, Constants.menu, Constants.item, Constants.icon))
                            .aria(hidden, true_).element());
                }
            } else {
                failSafeRemove(e, icon);
            }
        }

        if (onSelect != null) {
            onSelect.onSelect(value);
        }
        return this;
    }

    public SingleOptionsMenu<T> up() {
        element.classList.add(modifier(top));
        return this;
    }

    public SingleOptionsMenu<T> right() {
        menu.classList.add(modifier(alignRight));
        return this;
    }

    public SingleOptionsMenu<T> collapseOnSelect() {
        this.collapseOnSelect = true;
        return this;
    }

    @Override
    public SingleOptionsMenu<T> disable() {
        button.disabled = true;
        if (plain != null) {
            plain.classList.add(modifier(Constants.disabled));
        }
        return this;
    }

    @Override
    public SingleOptionsMenu<T> enable() {
        button.disabled = false;
        if (plain != null) {
            plain.classList.remove(modifier(Constants.disabled));
        }
        return this;
    }

    public void disable(T item) {
        HTMLButtonElement element = itemElement(item);
        if (element != null) {
            element.disabled = true;
        }
    }

    public void enable(T item) {
        HTMLButtonElement element = itemElement(item);
        if (element != null) {
            element.disabled = false;
        }
    }

    @Override
    public T value() {
        return value;
    }

    // ------------------------------------------------------ events

    public SingleOptionsMenu<T> onToggle(Consumer<Boolean> onToggle) {
        ceh.onToggle = onToggle;
        return this;
    }

    public SingleOptionsMenu<T> onSelect(SelectHandler<T> onSelect) {
        this.onSelect = onSelect;
        return this;
    }

    // ------------------------------------------------------ internals

    private HTMLButtonElement itemElement(T item) {
        String itemId = itemDisplay.itemId(item);
        return find(menu, By.data(singleOptionsMenuItem, itemId));
    }
}
