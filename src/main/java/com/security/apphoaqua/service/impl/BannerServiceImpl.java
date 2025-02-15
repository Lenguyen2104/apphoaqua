package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.response.banner.BannerAdminResponse;
import com.security.apphoaqua.entity.Banner;
import com.security.apphoaqua.repository.BannerRepository;
import com.security.apphoaqua.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.security.apphoaqua.core.response.ResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    @Override
    public ResponseBody<Object> getAllBanner() {
        List<Banner> banners = bannerRepository.findAll();
        List<BannerAdminResponse> bannerAdminResponses = banners.stream().map(banner -> BannerAdminResponse.builder()
                .id(banner.getId())
                .name(banner.getName())
                .url(banner.getUrl())
                .activated(banner.isActivated())
                .deleted(banner.isDeleted())
                .build()).toList();
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, bannerAdminResponses);
        return response;
    }
}
