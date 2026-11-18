package vn.fpt.fis.cmp.consent.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;


import vn.fpt.fis.cmp.consent.R;
import vn.fpt.fis.cmp.consent.api.ConsentJson;
import vn.fpt.fis.cmp.consent.model.ConsentConfig;
import vn.fpt.fis.cmp.consent.model.ConsentUiConfig;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, manifest = Config.NONE)
public class PrivacyPolicyLinkTest {

    private static ConsentConfig config(String policyJson) throws Exception {
        return ConsentJson.parseConfig(new JSONObject(
                "{\"data\":{\"code\":\"cp\",\"config\":{\"title\":\"T\"" + policyJson + ",\"items\":[]}}}"));
    }

    private static ConsentUiConfig ui(String url) {
        return new ConsentUiConfig(null, null, null, url, null);
    }

    @Test public void parsesPrivacyPolicyUrlFromConfigResponse() throws Exception {
        ConsentConfig config = config(",\"privacyPolicyUrl\":\"https://biznext.vn/chinh-sach-bao-mat/\"");
        assertEquals("https://biznext.vn/chinh-sach-bao-mat/", config.config.privacyPolicyUrl);
        assertNull(config(" ").config.privacyPolicyUrl);
    }

    @Test public void acceptsOnlyAbsoluteWebLinks() {
        assertEquals("https://a.vn/p", ui("  https://a.vn/p ").privacyPolicyLink());
        assertEquals("HTTP://a.vn", ui("HTTP://a.vn").privacyPolicyLink());
        assertNull(ui(null).privacyPolicyLink());
        assertNull(ui("").privacyPolicyLink());
        assertNull(ui("a.vn/policy").privacyPolicyLink());
        assertNull(ui("javascript:alert(1)").privacyPolicyLink());
        assertNull(ui("file:///data/x.html").privacyPolicyLink());
        assertNull(ui("intent://x#Intent;end").privacyPolicyLink());
        assertNull(ui("https://").privacyPolicyLink());
    }

    @Test public void showsLinkOnlyWhenConfigHasValidUrl() throws Exception {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        ConsentFormView form = new ConsentFormView(activity);
        TextView link = form.findViewById(R.id.cmp_tv_privacy_policy);

        form.bind(config(",\"privacyPolicyUrl\":\"https://biznext.vn/chinh-sach-bao-mat/\""), null);
        assertEquals(View.VISIBLE, link.getVisibility());
        assertEquals("Xem Chính sách quyền riêng tư", link.getText().toString());
        assertEquals("https://biznext.vn/chinh-sach-bao-mat/", form.privacyPolicyUrl());

        // Form nhung trong app (an nut gui): chan khoi van hien de giu link.
        form.setActionsVisible(false);
        assertEquals(View.VISIBLE, form.findViewById(R.id.cmp_footer).getVisibility());

        form.bind(config(",\"privacyPolicyUrl\":\"not a url\""), null);
        assertEquals(View.GONE, link.getVisibility());
        assertNull(form.privacyPolicyUrl());
        assertEquals(View.GONE, form.findViewById(R.id.cmp_footer).getVisibility());
    }

    @Test public void listenerCanHandleClickOtherwiseBrowserOpens() throws Exception {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        ConsentFormView form = new ConsentFormView(activity);
        form.bind(config(",\"privacyPolicyUrl\":\"https://a.vn/policy\""), null);
        TextView link = form.findViewById(R.id.cmp_tv_privacy_policy);

        List<String> clicked = new ArrayList<>();
        form.setOnPrivacyPolicyClickListener(url -> clicked.add(url));
        link.performClick();
        assertEquals(1, clicked.size());
        assertNull(Shadows.shadowOf(activity).getNextStartedActivity());

        form.setOnPrivacyPolicyClickListener(null);
        link.performClick();
        Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals("https://a.vn/policy", intent.getDataString());
    }
}
