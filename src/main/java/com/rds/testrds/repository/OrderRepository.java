package com.rds.testrds.repository;

import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.entity.UserEntity;
import com.rds.testrds.vo.RequestVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    public Optional<OrderEntity> findById(Long Id);

    /**
     * joing이 아닌 테이블 자체를 불러서 조회하기.(조인으로 익셉션 방지 테스트)
     * @param name
     * @return
     */
    @Query("select od from OrderEntity od where od.user.userName in (select u.userName from UserEntity u where u.userName = :name)")
    public List<OrderEntity> getExistOrderInfoByUserName(@Param("name") String name);

    /**
     * 프록시객체 LazyException 테스트용도 (jsonIgnore를 통하여 load 오류 사전방지 )
     * 기본 join으로 엔티티 내용 조회(OrderEntity 값만 가져옴. userEntity의 값에 접근하면 LazyInitialization 익셉션 발생. -영속성 초기화되지않은 객체값이므로)
     * @param name
     * @return
     */
    @Query("select od from OrderEntity od join od.user")
    public List<OrderEntity> testGetOrdersByUserName(@Param("name") String name);

    /**
     * fetch join으로 조회
     * @param name
     * @return
     */
    @Query("select od from OrderEntity od join fetch od.user where od.user.userName = :name")
    public List<OrderEntity> getExistOrderInfoFetchJoin(@Param("name") String name);
}
