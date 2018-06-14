package com.mbr.chain.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AddressUtil {

    public static String toAddress(String address) {
        if (StringUtils.isNotEmpty(address)) {
            if (address.length() == 40) {
                return "0x" + address;
            } else if (address.length() == 42 && address.startsWith("0x")) {
                return address;
            } else if (address.length() < 40) {
                return "0x" + String.format("%40s", address).replace(" ", "0");
            } else {
                return address.length() > 40 ? "0x" + address.substring(address.length() - 40) : address;
            }
        } else {
            return "0x0000000000000000000000000000000000000000";
        }
    }

}
