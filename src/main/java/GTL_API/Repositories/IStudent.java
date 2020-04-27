package GTL_API.Repositories;

import GTL_API.Models.Entities.CoverTypeEntity;
import GTL_API.Models.Entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IStudent extends JpaRepository<StudentEntity, Integer> {
    Optional<StudentEntity> findByStudentId(int studentId);
    Optional<StudentEntity> findByGpaBetween(int bottomRange, int upperRange);
}
