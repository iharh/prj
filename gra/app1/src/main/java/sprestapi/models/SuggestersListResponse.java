package sprestapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Artyom.Gabeev on 9/11/15.
 */
@NoArgsConstructor
@AllArgsConstructor
public class SuggestersListResponse {

    @Getter
    private List<String> suggesters;

}
