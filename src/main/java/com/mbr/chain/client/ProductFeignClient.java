package com.mbr.chain.client;

import com.mbr.chain.client.dto.ProductResponse;
import com.mbr.chain.domain.dto.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "product-service")
public interface ProductFeignClient {

    /**
     * 根据ID查询 并且允许交易的货币
     *
     * @param coinId
     * @return
     */
    @PostMapping(value = "queryByCoinIdAndOnline/{coinId}")
    public BaseResult<ProductResponse> queryByCoinIdAndOnline(@PathVariable(value = "coinId") Long coinId);


    @PostMapping(value = "queryByTokenAddress")
    public BaseResult<ProductResponse> queryByTokenAddress(@RequestParam(value = "tokenAddress") String tokenAddress);

}
