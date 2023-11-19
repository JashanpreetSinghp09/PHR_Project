package com.example.phr_application;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WalletManagerTest {

    @Mock
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateWalletInBackground() {
        WalletManager.GenerateWalletCallback callback = mock(WalletManager.GenerateWalletCallback.class);
        WalletManager walletManager = WalletManager.getInstance(context);

        walletManager.generateWalletInBackground("testPassword", callback);

        // Verify that the callback methods are called
        verify(callback).onWalletGenerated(anyString(), anyString(), anyString());
        verify(callback, never()).onWalletGenerationFailed(anyString());
    }

    @Test
    public void testLoadWalletCredentials() {
        WalletManager.LoadWalletCallback callback = mock(WalletManager.LoadWalletCallback.class);
        WalletManager walletManager = WalletManager.getInstance(context);

        // Assuming you have a wallet file named "testWallet.json" in the test directory
        String testPassword = "testPassword";
        String testWalletName = "testWallet.json";

        walletManager.loadWalletCredentials(testPassword, testWalletName, callback);

        // Verify that the callback methods are called
        verify(callback).onWalletLoaded(anyString(), anyString(), anyString());
        verify(callback, never()).onWalletLoadFailed(anyString());
    }
}
