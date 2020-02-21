package fr.coppernic.lib.interactors.barcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;

import fr.coppernic.lib.interactors.robolectric.RobolectricTest;
import io.reactivex.disposables.Disposable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BarcodeInteractorTest extends RobolectricTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private BarcodeInteractor interactor;
    @Mock
    private Context context;
    @Mock
    private PackageManager pm;

    @Before
    public void setUp() throws Exception {
        when(context.getPackageManager()).thenReturn(pm);
        when(pm.getInstalledPackages(PackageManager.GET_META_DATA)).thenReturn(Collections.<PackageInfo>emptyList());
        interactor = new BarcodeInteractor(context);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void dispose() {
        Disposable d = interactor.listen().subscribe();
        verify(context).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
        d.dispose();
        verify(context).unregisterReceiver(any(BroadcastReceiver.class));
    }
}