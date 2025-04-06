package ro.msg.mobile_clone.rest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://192.168.254.225:3000", "http://localhost:3000"})
@RequestMapping("/api/v1/heartbeat")
@AllArgsConstructor
public class HeartbeatController {
    @GetMapping
    public ResponseEntity<Void> check() {
        return ResponseEntity.ok().build();
    }
}
