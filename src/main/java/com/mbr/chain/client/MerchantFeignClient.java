package com.mbr.chain.client;


import com.mbr.chain.client.dto.WithdrawRequest;
import com.mbr.chain.client.dto.WithdrawResponse;
import com.mbr.chain.domain.dto.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "merchant-service")
public interface MerchantFeignClient {

    @PostMapping(value = "withdraw/checkMerchant")
    @ResponseBody
    public BaseResult<WithdrawResponse> checkMerchant(@RequestBody WithdrawRequest request);
}
