package com.sagar.request;

import lombok.Data;

@Data
public class CreateEventRequest {
    public String name;
    public String imageUrl;
    public String location;
    public String startedAt;
    public String endsAt;
}
