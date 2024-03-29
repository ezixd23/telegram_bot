package com.cursosrecomendados.telegram.domain.Global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class Global {
    @JsonProperty("data")
    private GlobalData data;

	public GlobalData getData() {
		return data;
	}

	public void setData(GlobalData data) {
		this.data = data;
	}
    
    
}