package org.patternfly.client.components;

import elemental2.dom.HTMLImageElement;

import static org.jboss.gwt.elemento.core.Elements.img;
import static org.patternfly.client.resources.CSS.component;
import static org.patternfly.client.resources.Constants.avatar;

/**
 * PatternFly avatar component.
 *
 * @see <a href= "https://www.patternfly.org/v4/documentation/react/components/avatar/">https://www.patternfly.org/v4/documentation/react/components/avatar</a>
 */
public class Avatar extends BaseComponent<HTMLImageElement, Avatar> {

    Avatar(String src, String alt) {
        super(img(src).css(component(avatar)).element(), "Avatar");
        element.alt = alt;
    }

    @Override
    public Avatar that() {
        return this;
    }
}
