package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Collaboration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Collaboration {
    public Collaboration(String name, MicrosoftTeams microsoftTeams) {
        this.name = name;
        this.microsoftTeams = microsoftTeams;
    }

    private String name;
    private MicrosoftTeams microsoftTeams;

    public Collaboration() {

    }
}

