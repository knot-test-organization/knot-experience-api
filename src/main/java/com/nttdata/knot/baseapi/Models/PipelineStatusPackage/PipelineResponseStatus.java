package com.nttdata.knot.baseapi.Models.PipelineStatusPackage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineResponseStatus {
    private String key; // This should be the unique identifier for your document

    private Value value;

    // private Value value;

    public PipelineResponseStatus() {

    }

    public PipelineResponseStatus(String key, Value value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "PipelineStatus [key=" + key + ", end_time=" + value.getEnd_time() + ", pipeline_id="
                + value.getPipeline_id() + ", start_time=" + value.getStart_time() + ", status=" + value.getStatus()
                + "]";
    }
}
