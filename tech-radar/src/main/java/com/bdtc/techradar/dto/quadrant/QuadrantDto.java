package com.bdtc.techradar.dto.quadrant;

import com.bdtc.techradar.model.Quadrant;

public record QuadrantDto(
        String id,
        String name,
        String title,
        String color,
        String txtColor,
        Integer position,
        String description
) {
    public QuadrantDto(Quadrant quadrant) {
        this(
                quadrant.getId(),
                quadrant.getName(),
                quadrant.getTitle(),
                quadrant.getColor(),
                quadrant.getTxtColor(),
                quadrant.getPosition(),
                quadrant.getDescription()
        );
    }
}
