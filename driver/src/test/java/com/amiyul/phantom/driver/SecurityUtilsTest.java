/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 *
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package com.amiyul.phantom.driver;

import static com.amiyul.phantom.api.Utils.decode;
import static com.amiyul.phantom.api.Utils.encode;
import static com.amiyul.phantom.driver.SecurityConstants.ALG;
import static org.junit.Assert.assertArrayEquals;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.junit.Test;

import com.amiyul.phantom.api.Utils;

public class SecurityUtilsTest {
	
	private static final byte[] ORIGINAL_DATA = "cool".getBytes(StandardCharsets.UTF_8);
	
	private static final byte[] CYPHER_DATA = ("UzuQxhwC59KsxeELMDxBDoQ4CwOp40NVimNmA2U/9ZX4SJeeX4GFSN0+bdPbbyNuD"
	        + "dmUZhY1xLT6dud6M88OnDYtRmcRPrdctFDJjN38dOCmXezEPvi1NGoo/xMoIAuupwQeKN20EpI1Xte9vLUK/F4Qq4ZK+1RzK3Q7ICQ"
	        + "Mf2TLpDHGA0gUPOTSmc7JAwr+M3AAJvbezxWorfhoK4qkx2XTrxRDr/GRPFW1ixJAhX43QmwvakqBiYueWg+cWNUYpnpzBO/cY4Sg1"
	        + "EHVtYjG6r2GBmyfcw5R8bFiwgSv9ohjGYin6Si5s4pN07Gz/h4U7IGDcCuA5vVMnN3yV9C2kp7f9AR55b1VjwFi/EYxhOh6j16HR+h"
	        + "hfN9UV19ZCQGRLAQANO9BB1mpCI7g6cz2WY91RIIYJTNgw8cMOGdB/vQNY1zAvYUVfROC/wglMepyiBpgk/F/3IXmVY6iuSE7W9PcN"
	        + "HgqqfapvPHBbFDVO8rSAPb+f4wIGs0Tz7mgjVvCGmhGAtHteSOrjU8kQbB0hxbqG5SrrgI1EIiGRV1BQd8EsUiIUGkP+WThS4oQuZ+"
	        + "yIQrYCX4gSB6v3KYg4pAUinLJPvfwpohvp9cdZ1nL/odaufV3KXDJ9KVPL+PIIVhE+1N6H2dblFsidr+OH/wxGPoesJm9DYvMCdteS"
	        + "YfwgAo=").getBytes(StandardCharsets.UTF_8);
	
	private static final byte[] PUBLIC_KEY = ("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgSif+37oNjukecR4hTGh3lWVS"
	        + "X3fzFvNkSgPn0y4UJtp4X+rCjb3bo3oZQypSSiN34QHSmqE6t5uAlna2PBvUaot2CbAfvwzraHtCaVB68PYSqcdnyY60jFFLl0K7l5p"
	        + "5Q37rnO8+Bb2D3SkMjJq4eB9nZgvEfnCgHFpOi2iYhE4vQ27dXbwf0kfIkI7WqSW9CWUlYmrAzFkUW0xcWeezwiQ/5zuRgpOvMxOOHK"
	        + "5pCigMb6AzIVidgLon4aWXHxJC2VDd4vm2tDuGS3yB/weiSdJqiM/CqaYYf4v+Fc5+l9HjXEcqI1kzg8UE9wme9awc8ck6NeQ6zSX9/"
	        + "GvO/o7NiMaZ+N2/0/VAanm9JkAlbwcH5GMvlCa3lRebxoHUFTlYhHUwquzHUsdtIg8aCIUDHrdjbT9JQSjST4ZLBMTkZD1Ucc8bx9Pp"
	        + "h8WbkzCBU9ZzAV2ob+LzB/f4FFlbC1QH69BKxQnXcA3npNWS3qzZS98PYtcPbm8Eh9U6TG2WDoofwi0pO6iEYFYF958+W/TrbdrX+VJ"
	        + "avQ1zAiWrH2h+Q1IZb4YcAoPesDabCKXP66ESvSrEz6y7//ZhbrwZWg8U5JpT9l9h74GiMd/1NAq1w2M7roUWO7BSSIvjlpKes3pJh6"
	        + "vIUYdY0BfmAHRCTs8sUOyb5jgFi9dUdvDvYrnjoECAwEAAQ==").getBytes(StandardCharsets.UTF_8);
	
	private static final byte[] PRIVATE_KEY = ("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"
	        + "K41ucP7g==").getBytes(StandardCharsets.UTF_8);
	
	@Test
	public void encrypt_shouldEncryptAString() throws Exception {
		PrivateKey key = KeyFactory.getInstance(ALG).generatePrivate(new PKCS8EncodedKeySpec(decode(PRIVATE_KEY)));
		assertArrayEquals(CYPHER_DATA, encode(SecurityUtils.encrypt(ORIGINAL_DATA, key)));
	}
	
	@Test
	public void decrypt_shouldDecryptAString() throws Exception {
		PublicKey key = KeyFactory.getInstance(ALG).generatePublic(new X509EncodedKeySpec(Utils.decode(PUBLIC_KEY)));
		assertArrayEquals(ORIGINAL_DATA, SecurityUtils.decrypt(decode(CYPHER_DATA), key));
	}
	
	private void generateTestKeyPair() throws Exception {
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(1024);
		KeyPair keyPair = gen.generateKeyPair();
		try (FileOutputStream fos = new FileOutputStream("private.key")) {
			fos.write(Utils.encode(keyPair.getPrivate().getEncoded()));
		}
		try (FileOutputStream fos = new FileOutputStream("public.key")) {
			fos.write(Utils.encode(keyPair.getPublic().getEncoded()));
		}
	}
	
}
