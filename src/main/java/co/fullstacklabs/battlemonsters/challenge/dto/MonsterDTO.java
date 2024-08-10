package co.fullstacklabs.battlemonsters.challenge.dto;

import com.univocity.parsers.annotations.Parsed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonsterDTO {
    private Integer id;

    @Parsed
    private String name;

    @Parsed
    private Integer attack;

    @Parsed
    private Integer defense;

    @Parsed
    private Integer hp;

    @Parsed
    private Integer speed;

    @Parsed
    private String imageUrl;
}
