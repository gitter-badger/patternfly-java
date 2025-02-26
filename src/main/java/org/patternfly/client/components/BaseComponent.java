package org.patternfly.client.components;

import elemental2.dom.HTMLElement;
import org.jboss.gwt.elemento.core.IsElement;
import org.jboss.gwt.elemento.core.builder.ElementBuilder;
import org.jboss.gwt.elemento.core.builder.TypedBuilder;
import org.patternfly.client.core.Ouia;

abstract class BaseComponent<E extends HTMLElement, B extends ElementBuilder<E, B>>
        extends ElementBuilder<E, B>
        implements TypedBuilder<E, B>, IsElement<E> {

    BaseComponent(E element, String component) {
        this(element, component, null);
    }

    BaseComponent(E element, String component, String id) {
        super(element);
        Ouia.populate(element, component, id);
    }

    @Override
    public B id() {
        super.id();
        Ouia.id(element, element.id);
        return that();
    }

    @Override
    public B id(String id) {
        super.id(id);
        Ouia.id(element, element.id);
        return that();
    }
}
