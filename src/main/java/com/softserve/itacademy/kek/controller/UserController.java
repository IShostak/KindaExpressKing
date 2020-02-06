package com.softserve.itacademy.kek.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
public class UserController extends DefaultController {
    final Logger logger = Logger.getLogger(UserController.class);

    // Build Response (stub, temporary method)
    private String getJSON(String id, String status) {
        JSONObject json = new JSONObject();
        json.put("userID", id);
        json.put("status", status);
        return json.toString();
    }

    /**
     * Get information about users
     *
     * @return list of user objects as a JSON
     */
    @GetMapping
    public ResponseEntity<String> getUserList() {
        logger.info("Client requested the list of all users");

        JSONObject json = new JSONObject();
        json.append("userID", "1").append("userID", "2").append("userID", "3");
        json.put("status", "received");

        logger.info("Sending list of all users to the client");
        return ResponseEntity.ok(json.toString());
    }

    /**
     * Returns information about the requested user
     *
     * @param id user ID from the URN
     * @return user objects as a JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) {
        logger.info("Sending the user(" + id + ") to the client");
        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Creates a new user
     *
     * @param body user object as a JSON
     * @return created user object as a JSON
     */
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody String body) {
        logger.info("Sending the created user to the client");
        return ResponseEntity.ok(body);
    }

    /**
     * Modifies information of the specified user
     *
     * @param id   user ID from the URN
     * @param body user object as a JSON
     * @return modified user object as a JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyUser(@PathVariable String id, @RequestBody String body) {
        logger.info("Sending the modified user to the client");
        return ResponseEntity.ok(body);
    }

    /**
     * Removes the specified user
     *
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        logger.info("User (" + id + ") successfully deleted");
        return ResponseEntity.ok(getJSON(id, "deleted"));
    }

    /**
     * Finds addresses of the specific user
     *
     * @param id user ID from the URN
     * @return list of the address objects as a JSON
     */
    @GetMapping("/{id}/addresses")
    public ResponseEntity<String> getUserAddresses(@PathVariable String id) {
        logger.info("Sending the list of user(" + id + ") addresses to the client");
        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Adds a new addresses for the specific user
     *
     * @param id   user ID from the URN
     * @param body list of address objects as a JSON
     * @return list of the created address objects as a JSON
     */
    @PostMapping("/{id}/addresses")
    public ResponseEntity<String> addUserAddresses(@PathVariable String id, @RequestBody String body) {
        logger.info("Sending the created user(" + id + ") addresses to the client");
        return ResponseEntity.ok(body);
    }

    /**
     * Finds addresses of the specific user
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     * @return address object as a JSON
     */
    @GetMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> getUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Sending the user(" + id + ") address(" + addrGuid + ") to the client");
        return ResponseEntity.ok(getJSON(id, "received"));
    }


    /**
     * Modifies the specific user address tenant property
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     * @param body     address object as a JSON
     * @return modified address object as a JSON
     */
    @PutMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> modifyUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid, @RequestBody String body) {
        logger.info("Sending the modified user(" + id + ") address(" + addrGuid + ") to the client");
        return ResponseEntity.ok(body);
    }

    /**
     * Deletes the specific user address
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> deleteUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("User (" + id + ") address(" + addrGuid + ") successfully deleted");
        return ResponseEntity.ok(getJSON(id, "deleted"));
    }
}
