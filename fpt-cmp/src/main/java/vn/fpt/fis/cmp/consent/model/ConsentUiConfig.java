package vn.fpt.fis.cmp.consent.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Khoi {@code config} trong response /config: tieu de, mo ta, nhan nut, link chinh sach, danh sach purpose. */
public final class ConsentUiConfig implements Serializable {

    public final String title;
    public final String description;
    public final String submitLabel;
    /**
     * Link chinh sach quyen rieng tu cua mau consent ma diem thu thap dang dung (cau hinh tren
     * portal CMP). Gia tri tho tu server; dung {@link #privacyPolicyLink()} de lay link hop le.
     */
    public final String privacyPolicyUrl;
    public final List<ConsentItem> items;

    public ConsentUiConfig(String title, String description, String submitLabel, List<ConsentItem> items) {
        this(title, description, submitLabel, null, items);
    }

    public ConsentUiConfig(String title, String description, String submitLabel,
                           String privacyPolicyUrl, List<ConsentItem> items) {
        this.title = title;
        this.description = description;
        this.submitLabel = submitLabel;
        this.privacyPolicyUrl = privacyPolicyUrl;
        this.items = items == null ? Collections.<ConsentItem>emptyList() : items;
    }

    /** Link chinh sach neu la URL http/https day du; null khi trong hoac khong hop le (khong hien link). */
    public String privacyPolicyLink() {
        if (privacyPolicyUrl == null) {
            return null;
        }
        String url = privacyPolicyUrl.trim();
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.US);
            boolean web = scheme.equals("http") || scheme.equals("https");
            return web && uri.getHost() != null && !uri.getHost().isEmpty() ? url : null;
        } catch (Exception e) {
            return null;
        }
    }
}
