package com.epam.hibernate;

import com.epam.hibernate.dto.*;
import com.epam.hibernate.repository.TraineeRepository;
import com.epam.hibernate.repository.TrainerRepository;
import com.epam.hibernate.repository.TrainingRepository;
import com.epam.hibernate.repository.UserRepository;
import com.epam.hibernate.service.GymService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional

class GymServiceTest {

    @Autowired
    private GymService gymService;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateTrainerProfile() {
        CredentialsResponse response = gymService.createTrainer(
                new CreateTrainerRequest("John", "Smith", "Fitness")
        );

        assertThat(response.username()).isEqualTo("John.Smith");
        assertThat(response.password()).isNotBlank();
        assertThat(trainerRepository.findByUserUsername("John.Smith")).isPresent();
    }

    @Test
    void shouldCreateTraineeProfile() {
        CredentialsResponse response = gymService.createTrainee(
                new CreateTraineeRequest("Alex", "Brown", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        assertThat(response.username()).isEqualTo("Alex.Brown");
        assertThat(response.password()).isNotBlank();
        assertThat(traineeRepository.findByUserUsername("Alex.Brown")).isPresent();
    }

    @Test
    void shouldAuthenticateCreatedTrainer() {
        CredentialsResponse response = gymService.createTrainer(
                new CreateTrainerRequest("Mike", "Wilson", "Yoga")
        );

        boolean result = gymService.authenticate(response.username(), response.password());

        assertThat(result).isTrue();
    }

    @Test
    void shouldAuthenticateCreatedTrainee() {
        CredentialsResponse response = gymService.createTrainee(
                new CreateTraineeRequest("Sara", "Lee", LocalDate.of(1999, 5, 10), "Seoul")
        );

        boolean result = gymService.authenticate(response.username(), response.password());

        assertThat(result).isTrue();
    }

    @Test
    void shouldChangePassword() {
        CredentialsResponse response = gymService.createTrainee(
                new CreateTraineeRequest("Password", "User", LocalDate.of(2001, 1, 1), "Tashkent")
        );

        gymService.changePassword(
                new ChangePasswordRequest(response.username(), response.password(), "newPass123")
        );

        assertThat(gymService.authenticate(response.username(), "newPass123")).isTrue();
    }

    @Test
    void shouldUpdateTrainerProfile() {
        CredentialsResponse response = gymService.createTrainer(
                new CreateTrainerRequest("Update", "Trainer", "Fitness")
        );

        gymService.updateTrainer(
                new UpdateTrainerRequest(
                        response.username(),
                        response.password(),
                        "Updated",
                        "Trainer",
                        "Yoga",
                        true
                )
        );

        var trainer = trainerRepository.findByUserUsername(response.username()).orElseThrow();

        assertThat(trainer.getUser().getFirstName()).isEqualTo("Updated");
        assertThat(trainer.getSpecialization()).isEqualTo("Yoga");
    }

    @Test
    void shouldUpdateTraineeProfile() {
        CredentialsResponse response = gymService.createTrainee(
                new CreateTraineeRequest("Update", "Trainee", LocalDate.of(2000, 1, 1), "Old address")
        );

        gymService.updateTrainee(
                new UpdateTraineeRequest(
                        response.username(),
                        response.password(),
                        "Updated",
                        "Trainee",
                        LocalDate.of(2002, 2, 2),
                        "New address",
                        true
                )
        );

        var trainee = traineeRepository.findByUserUsername(response.username()).orElseThrow();

        assertThat(trainee.getUser().getFirstName()).isEqualTo("Updated");
        assertThat(trainee.getAddress()).isEqualTo("New address");
    }

    @Test
    void shouldDeactivateAndActivateTrainee() {
        CredentialsResponse admin = gymService.createTrainer(
                new CreateTrainerRequest("Admin", "Trainer", "Fitness")
        );

        CredentialsResponse traineeResponse = gymService.createTrainee(
                new CreateTraineeRequest("Active", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        gymService.deactivateTrainee(admin.username(), admin.password(), traineeResponse.username());

        assertThat(traineeRepository.findByUserUsername(traineeResponse.username())
                .orElseThrow()
                .getUser()
                .getIsActive()).isFalse();

        gymService.activateTrainee(admin.username(), admin.password(), traineeResponse.username());

        assertThat(traineeRepository.findByUserUsername(traineeResponse.username())
                .orElseThrow()
                .getUser()
                .getIsActive()).isTrue();
    }

    @Test
    void shouldNotDeactivateAlreadyInactiveTrainee() {
        CredentialsResponse admin = gymService.createTrainer(
                new CreateTrainerRequest("Admin2", "Trainer2", "Fitness")
        );

        CredentialsResponse traineeResponse = gymService.createTrainee(
                new CreateTraineeRequest("Inactive", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        gymService.deactivateTrainee(admin.username(), admin.password(), traineeResponse.username());

        assertThatThrownBy(() ->
                gymService.deactivateTrainee(admin.username(), admin.password(), traineeResponse.username())
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDeactivateAndActivateTrainer() {
        CredentialsResponse admin = gymService.createTrainee(
                new CreateTraineeRequest("Admin", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        CredentialsResponse trainerResponse = gymService.createTrainer(
                new CreateTrainerRequest("Active", "Trainer", "Fitness")
        );

        gymService.deactivateTrainer(admin.username(), admin.password(), trainerResponse.username());

        assertThat(trainerRepository.findByUserUsername(trainerResponse.username())
                .orElseThrow()
                .getUser()
                .getIsActive()).isFalse();

        gymService.activateTrainer(admin.username(), admin.password(), trainerResponse.username());

        assertThat(trainerRepository.findByUserUsername(trainerResponse.username())
                .orElseThrow()
                .getUser()
                .getIsActive()).isTrue();
    }

    @Test
    void shouldAddTraining() {
        CredentialsResponse trainer = gymService.createTrainer(
                new CreateTrainerRequest("Training", "Trainer", "Fitness")
        );

        CredentialsResponse trainee = gymService.createTrainee(
                new CreateTraineeRequest("Training", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        gymService.addTraining(
                new AddTrainingRequest(
                        trainer.username(),
                        trainer.password(),
                        trainee.username(),
                        trainer.username(),
                        "Morning training",
                        "Fitness",
                        LocalDate.of(2026, 5, 18),
                        60
                )
        );

        assertThat(trainingRepository.findAll()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldGetTraineeTrainingsByCriteria() {
        CredentialsResponse trainer = gymService.createTrainer(
                new CreateTrainerRequest("Filter", "Trainer", "Fitness")
        );

        CredentialsResponse trainee = gymService.createTrainee(
                new CreateTraineeRequest("Filter", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        gymService.addTraining(
                new AddTrainingRequest(
                        trainer.username(),
                        trainer.password(),
                        trainee.username(),
                        trainer.username(),
                        "Filtered training",
                        "Fitness",
                        LocalDate.of(2026, 5, 18),
                        45
                )
        );

        List<TrainingResponse> result = gymService.getTraineeTrainings(
                trainer.username(),
                trainer.password(),
                trainee.username(),
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 30),
                trainer.username(),
                "Fitness"
        );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).trainingName()).isEqualTo("Filtered training");
    }

    @Test
    void shouldGetTrainerTrainingsByCriteria() {
        CredentialsResponse trainer = gymService.createTrainer(
                new CreateTrainerRequest("List", "Trainer", "Fitness")
        );

        CredentialsResponse trainee = gymService.createTrainee(
                new CreateTraineeRequest("List", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        gymService.addTraining(
                new AddTrainingRequest(
                        trainer.username(),
                        trainer.password(),
                        trainee.username(),
                        trainer.username(),
                        "Trainer list training",
                        "Fitness",
                        LocalDate.of(2026, 5, 18),
                        30
                )
        );

        List<TrainingResponse> result = gymService.getTrainerTrainings(
                trainee.username(),
                trainee.password(),
                trainer.username(),
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 30),
                trainee.username()
        );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).trainingName()).isEqualTo("Trainer list training");
    }

    @Test
    void shouldUpdateTraineeTrainersList() {
        CredentialsResponse auth = gymService.createTrainee(
                new CreateTraineeRequest("Auth", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        CredentialsResponse trainee = gymService.createTrainee(
                new CreateTraineeRequest("Assigned", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        CredentialsResponse trainer = gymService.createTrainer(
                new CreateTrainerRequest("Assigned", "Trainer", "Fitness")
        );

        gymService.updateTraineeTrainers(
                new UpdateTraineeTrainersRequest(
                        auth.username(),
                        auth.password(),
                        trainee.username(),
                        List.of(trainer.username())
                )
        );

        var updatedTrainee = traineeRepository.findByUserUsername(trainee.username()).orElseThrow();

        assertThat(updatedTrainee.getTrainers()).hasSize(1);
    }

    @Test
    void shouldGetNotAssignedTrainers() {
        CredentialsResponse auth = gymService.createTrainee(
                new CreateTraineeRequest("NotAssigned", "Auth", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        CredentialsResponse trainee = gymService.createTrainee(
                new CreateTraineeRequest("NotAssigned", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        CredentialsResponse trainer = gymService.createTrainer(
                new CreateTrainerRequest("NotAssigned", "Trainer", "Fitness")
        );

        List<TrainerResponse> result = gymService.getNotAssignedTrainers(
                auth.username(),
                auth.password(),
                trainee.username()
        );

        assertThat(result)
                .extracting(TrainerResponse::username)
                .contains(trainer.username());
    }

    @Test
    void shouldHardDeleteTraineeAndCascadeTrainings() {
        CredentialsResponse trainer = gymService.createTrainer(
                new CreateTrainerRequest("Delete", "Trainer", "Fitness")
        );

        CredentialsResponse trainee = gymService.createTrainee(
                new CreateTraineeRequest("Delete", "Trainee", LocalDate.of(2000, 1, 1), "Tashkent")
        );

        gymService.addTraining(
                new AddTrainingRequest(
                        trainer.username(),
                        trainer.password(),
                        trainee.username(),
                        trainer.username(),
                        "Delete cascade training",
                        "Fitness",
                        LocalDate.of(2026, 5, 18),
                        50
                )
        );

        assertThat(trainingRepository.findAll()).isNotEmpty();

        gymService.deleteTrainee(trainer.username(), trainer.password(), trainee.username());

        assertThat(traineeRepository.findByUserUsername(trainee.username())).isEmpty();
        assertThat(trainingRepository.findAll().stream()
                .noneMatch(training -> training.getTrainingName().equals("Delete cascade training")))
                .isTrue();
    }
}