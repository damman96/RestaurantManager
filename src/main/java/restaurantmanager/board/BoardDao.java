package restaurantmanager.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardDao extends JpaRepository<Board, Long> {
}
