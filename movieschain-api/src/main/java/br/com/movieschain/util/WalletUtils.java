package br.com.movieschain.util;

import org.web3j.crypto.Sign;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class WalletUtils {

    public static boolean verifySignature(String address, String signedMessage, String originalMessage) {
        try {
            String prefix = "\u0019Ethereum Signed Message:\n" + originalMessage.length();
            byte[] messageHash = Hash.sha3((prefix + originalMessage).getBytes());

            Sign.SignatureData signatureData = extractSignatureData(signedMessage);

            BigInteger publicKeyRecovered = Sign.signedMessageHashToKey(messageHash, signatureData);

            String recoveredAddress = "0x" + Keys.getAddress(publicKeyRecovered);

            return recoveredAddress.equalsIgnoreCase(address);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Sign.SignatureData extractSignatureData(String signature) {
        byte[] sigBytes = Numeric.hexStringToByteArray(signature);

        byte v = sigBytes[64];
        if (v < 27) {
            v += 27;
        }

        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(sigBytes, 0, r, 0, 32);
        System.arraycopy(sigBytes, 32, s, 0, 32);

        return new Sign.SignatureData(v, r, s);
    }
}
