package com.goodee.corpdesk.approval.entity;

import com.goodee.corpdesk.approval.dto.ApprovalDTO;
import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import com.goodee.corpdesk.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@ToString
@SuperBuilder
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "approval_form")
@DynamicInsert
@DynamicUpdate
public class ApprovalForm extends BaseEntity {
	
	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO 서버실행시 기본값 넣기위해 임시로 주석처리
	private Integer approvalFormId;
	
	@Column(nullable = false)
	private String formTitle;

//	@Column(nullable = false) @Lob 대신 아래의 어노테이션 적용
//	이유: String 타입에 @Lob을 붙여도 text 타입 대신 tinytext 타입으로 매핑됨
//	대용량 데이터를 담기 위해 tinytext 대신 longtext 사용
	@Column(columnDefinition = "longtext")
	private String formContent;

    public ResApprovalDTO toResApprovalDTO() {
        return ResApprovalDTO.builder()
                .approvalFormId(approvalFormId)
                .formTitle(formTitle)
                .formContent(formContent)
                .build();
    }
	
}
