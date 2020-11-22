package com.abubusoft.powertrainer.backend.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = WebPathConstants.API_ENTRYPOINT + "/env")
public class EnvironmentController {
//  @GetMapping
//  public Map<String, String> env() {
//    return System.getenv();
//  }

}
