package com.fellasbar.api.dto;

import com.fellasbar.api.model.Venue;
import com.fellasbar.api.model.OperatingHours;
import com.fellasbar.api.model.Special;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record VenueResponse(
    Long id,
    String name,
    String type,
    String address,
    Double latitude,
    Double longitude,
    String phone,
    String website,
    String description,
    String imageUrl,
    Double rating,
    Integer reviewCount,
    Boolean isOpen,
    Map<String, String> hours,
    List<SpecialResponse> specials
) {

    public record SpecialResponse(
        Long id,
        String name,
        String description,
        BigDecimal originalPrice,
        BigDecimal specialPrice,
        String dayOfWeek
    ) {
        public static SpecialResponse from(Special s) {
            return new SpecialResponse(
                s.getId(),
                s.getName(),
                s.getDescription(),
                s.getOriginalPrice(),
                s.getSpecialPrice(),
                s.getDayOfWeek()
            );
        }
    }

    public static VenueResponse from(Venue v) {
        Map<String, String> hoursMap = new LinkedHashMap<>();
        for (OperatingHours oh : v.getOperatingHours()) {
            hoursMap.put(oh.getDayOfWeek(), oh.getHours());
        }

        List<SpecialResponse> specialsList = v.getSpecials().stream()
            .map(SpecialResponse::from)
            .collect(Collectors.toList());

        return new VenueResponse(
            v.getId(),
            v.getName(),
            v.getType(),
            v.getAddress(),
            v.getLatitude(),
            v.getLongitude(),
            v.getPhone(),
            v.getWebsite(),
            v.getDescription(),
            v.getImageUrl(),
            v.getRating(),
            v.getReviewCount(),
            v.getIsOpen(),
            hoursMap,
            specialsList
        );
    }
}
