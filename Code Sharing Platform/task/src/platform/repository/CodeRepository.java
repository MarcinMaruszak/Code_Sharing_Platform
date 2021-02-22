package platform.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import platform.model.Code;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, Long> {

    //List<Code> findAllByViewableTrueAndValidUntilAfterOrderByDateDesc(Pageable pageable, LocalDateTime dateTime);

    List<Code> findTop10ByOrderByDateDesc();

    Optional<Code> findByUuid(UUID uuid);

    @Transactional
    void deleteByUuid(UUID uuid);

    @Transactional
    void deleteAllByValidUntilBefore(LocalDateTime localDateTime);
}
