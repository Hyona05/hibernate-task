package com.epam.hibernate.controller;

import com.epam.hibernate.dto.*;
import com.epam.hibernate.service.GymService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GymController {

    private final GymService gymService;

    @PostMapping("/trainers")
    public CredentialsResponse createTrainer(@Valid @RequestBody CreateTrainerRequest request) {
        return gymService.createTrainer(request);
    }

    @PostMapping("/trainees")
    public CredentialsResponse createTrainee(@Valid @RequestBody CreateTraineeRequest request) {
        return gymService.createTrainee(request);
    }

    @PostMapping("/auth")
    public boolean authenticate(@Valid @RequestBody AuthRequest request) {
        return gymService.authenticate(request.username(), request.password());
    }

    @PostMapping("/auth/trainer")
    public boolean authenticateTrainer(@Valid @RequestBody AuthRequest request) {
        return gymService.authenticateTrainer(request.username(), request.password());
    }

    @PostMapping("/auth/trainee")
    public boolean authenticateTrainee(@Valid @RequestBody AuthRequest request) {
        return gymService.authenticateTrainee(request.username(), request.password());
    }

    @GetMapping("/trainers/{username}")
    public TrainerResponse getTrainer(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        return gymService.getTrainerByUsername(authUsername, password, username);
    }

    @GetMapping("/trainees/{username}")
    public TraineeResponse getTrainee(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        return gymService.getTraineeByUsername(authUsername, password, username);
    }

    @PutMapping("/password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        gymService.changePassword(request);
    }

    @PutMapping("/trainers")
    public TrainerResponse updateTrainer(@Valid @RequestBody UpdateTrainerRequest request) {
        return gymService.updateTrainer(request);
    }

    @PutMapping("/trainees")
    public TraineeResponse updateTrainee(@Valid @RequestBody UpdateTraineeRequest request) {
        return gymService.updateTrainee(request);
    }

    @PatchMapping("/trainees/{username}/activate")
    public void activateTrainee(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        gymService.activateTrainee(authUsername, password, username);
    }

    @PatchMapping("/trainees/{username}/deactivate")
    public void deactivateTrainee(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        gymService.deactivateTrainee(authUsername, password, username);
    }

    @PatchMapping("/trainers/{username}/activate")
    public void activateTrainer(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        gymService.activateTrainer(authUsername, password, username);
    }

    @PatchMapping("/trainers/{username}/deactivate")
    public void deactivateTrainer(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        gymService.deactivateTrainer(authUsername, password, username);
    }

    @DeleteMapping("/trainees/{username}")
    public void deleteTrainee(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        gymService.deleteTrainee(authUsername, password, username);
    }

    @PostMapping("/trainings")
    public void addTraining(@Valid @RequestBody AddTrainingRequest request) {
        gymService.addTraining(request);
    }

    @GetMapping("/trainees/{username}/trainings")
    public List<TrainingResponse> getTraineeTrainings(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType
    ) {
        return gymService.getTraineeTrainings(
                authUsername,
                password,
                username,
                fromDate,
                toDate,
                trainerName,
                trainingType
        );
    }

    @GetMapping("/trainers/{username}/trainings")
    public List<TrainingResponse> getTrainerTrainings(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String traineeName
    ) {
        return gymService.getTrainerTrainings(
                authUsername,
                password,
                username,
                fromDate,
                toDate,
                traineeName
        );
    }

    @GetMapping("/trainees/{username}/not-assigned-trainers")
    public List<TrainerResponse> getNotAssignedTrainers(
            @RequestParam String authUsername,
            @RequestParam String password,
            @PathVariable String username
    ) {
        return gymService.getNotAssignedTrainers(authUsername, password, username);
    }

    @PutMapping("/trainees/trainers")
    public TraineeResponse updateTraineeTrainers(@Valid @RequestBody UpdateTraineeTrainersRequest request) {
        return gymService.updateTraineeTrainers(request);
    }
}