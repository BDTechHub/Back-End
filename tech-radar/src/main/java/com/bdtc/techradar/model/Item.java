package com.bdtc.techradar.model;


import com.bdtc.techradar.constant.Flag;
import com.bdtc.techradar.constant.Ring;
import com.bdtc.techradar.dto.item.ItemRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Flag flag;

    private boolean isActive;  // featured

    private String author;
    private String authorEmail;
    private List<String> revisions; // revisions (list of author emails)

    private String title;

    private LocalDateTime creationDate;
    private LocalDateTime publicationDate; // release --> YYYY-MM-DD (confirm format)
    private LocalDateTime updateDate;

    private String name;

    @Enumerated(EnumType.STRING)
    private Ring ring;

    @ManyToOne
    @JoinColumn(name = "quadrant_id")
    private Quadrant quadrant;

    @Column(columnDefinition = "TEXT")
    private String body;

    public Item(ItemRequestDto itemRequestDto) {
        this.flag = itemRequestDto.flag();
        this.isActive = itemRequestDto.isActive();
        this.authorEmail = itemRequestDto.authorEmail();
        this.revisions = itemRequestDto.revisions();
        this.title = itemRequestDto.title();
        this.ring = itemRequestDto.ring();
        this.body = itemRequestDto.body();
    }
}