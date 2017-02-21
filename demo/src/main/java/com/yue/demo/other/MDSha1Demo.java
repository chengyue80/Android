package com.yue.demo.other;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.widget.TextView;

import com.yue.demo.RootActivity;
import com.yue.demo.util.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * 获取应用的MD5 和 sha1信息
 * 
 * @author chengyue
 * 
 */
public class MDSha1Demo extends RootActivity {

	TextView text = null;

	StringBuffer sb = new StringBuffer();

	private String TAG = MDSha1Demo.class.getSimpleName();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		text = new TextView(this);
		setContentView(text);

		try {

			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), PackageManager.GET_SIGNATURES);

			Signature signatures = pi.signatures[0];

			String res = getSignatures(signatures, "MD5");
			LogUtil.e(TAG, "apk md5 = " + res);
			sb.append("apk md5 = " + res);

			String res2 = getSignatures(signatures, "SHA1");
			LogUtil.e(TAG, "apk SHA1 = " + res2);
			sb.append("\napk SHA1 = " + res2);

			ByteArrayInputStream bais = new ByteArrayInputStream(
					signatures.toByteArray());

			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			X509Certificate cert = (X509Certificate) cf
					.generateCertificate(bais);

			String sigAlgName = cert.getSigAlgName();

			String subjectDN = cert.getSubjectDN().toString();

			LogUtil.e(TAG, "sigAlgName = " + sigAlgName);

			LogUtil.e(TAG, "subjectDN = " + subjectDN);

			sb.append("\n sigAlgName = " + sigAlgName);

			sb.append("\n subjectDN = " + subjectDN);

			bais.close();

		} catch (NameNotFoundException e) {

			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (CertificateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		text.setText(sb.toString());

	}

	private char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 获取签名信息
	 * 
	 * @param signatures
	 * @param type
	 *            MD5 或 SHA1
	 * @return 签名信息的MD5或SHA1信息
	 * @throws NoSuchAlgorithmException
	 */
	public String getSignatures(Signature signatures, String type)
			throws NoSuchAlgorithmException {
		StringBuffer buf = new StringBuffer();

		MessageDigest md = MessageDigest.getInstance(type);
		md.update(signatures.toByteArray());
		byte[] digest = md.digest();
		int len = digest.length;
		for (int i = 0; i < len; i++) {
			int high = ((digest[i] & 0xf0) >> 4);
			int low = (digest[i] & 0x0f);
			buf.append(hexChars[high]);
			buf.append(hexChars[low]);
			if (i < len - 1) {
				buf.append(":");
			}
		}
		return buf.toString();

	}

}
