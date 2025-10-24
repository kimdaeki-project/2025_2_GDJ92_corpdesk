package com.goodee.corpdesk.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

    @Value("${cat.board}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }


    // 공지 게시글
  @GetMapping("/notice")
  public String noticeList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
    
    Page<Board> page = boardService.getNoticeBoards(pageable);
    
    model.addAttribute("page", page);
    model.addAttribute("departmentId", 0);
    model.addAttribute("title", "공지 게시판");
    model.addAttribute("post", page.getContent());
    model.addAttribute("writePath", "/board/notice/write");
    
    return "board/boardList";
  }

  // 공지 게시글 상세 페이지
  @GetMapping("/notice/{boardId}")
  public String noticeDetail(@PathVariable("boardId") Long boardId,
                             @AuthenticationPrincipal(expression = "username") String currentUsername,
                             Model model) {
    
    Board post = boardService.getBoardsDetailWithViewUp(boardId);
    model.addAttribute("post", post);
                      
    boolean isOwner = currentUsername != null && currentUsername.equals(post.getUsername());
    
    model.addAttribute("isOwner", isOwner);
    model.addAttribute("title", "공지 게시글");
    
    return "board/noticeDetail";
  }

  // 내 부서 게시글
  @GetMapping("/department")
  public String myDepartmentList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model) {

    Page<Board> page = boardService.getMyDepartmentBoards(pageable);
    
    model.addAttribute("page", page);
    model.addAttribute("title", "부서 게시판");
    model.addAttribute("post", page.getContent());
    model.addAttribute("writePath", "/board/department/write");
    
    return "board/boardList";
  }

  // 부서 게시글 상세
  @GetMapping("/department/{boardId}")
  public String departmentDetail(@PathVariable("boardId") Long boardId,
                                 @AuthenticationPrincipal(expression = "username") String currentUsername,
                                 Model model) {
    
    Board post = boardService.getBoardsDetailWithViewUp(boardId);
    model.addAttribute("post", post);

    boolean isOwner = currentUsername != null && currentUsername.equals(post.getUsername());
    
    model.addAttribute("isOwner", isOwner);
    model.addAttribute("title", "부서 게시글");
    
    return "board/departmentDetail";
  }

  // 공지 글쓰기 폼
  @GetMapping("/notice/write")
  public String noticeWriteForm(Model model) {
    
    model.addAttribute("title", "공지글 쓰기");
    
    return "board/noticeWrite";
  }

  // 공지글 등록
  @PostMapping("/notice")
  public String createNotice(Board board) {
    
    board.setDepartmentId(0);
    
    Board saved = boardService.createPost(board);
    
    return "redirect:/board/notice/" + saved.getBoardId();
  }

  // 부서 글쓰기 폼
  @GetMapping("/department/write")
  public String departmentWriteForm(Model model) {
    
    model.addAttribute("title", "부서 글쓰기");
    
    return "board/departmentWrite";
  }

  // 부서글 등록
  @PostMapping("/department")
  public String createDepartmentPost(Board board) {
    
    // departmentId가 null이면 Service에서 Authentication 기반으로 채움
    Board saved = boardService.createPost(board);
    
    return "redirect:/board/department/" + saved.getBoardId();
  }

  // 공지 수정 폼
  @GetMapping("/notice/{boardId}/edit")
  public String noticeEditForm(@PathVariable("boardId") Long boardId, Model model) {
    
    Board post = boardService.getBoardsDetail(boardId);
    
    model.addAttribute("post", post);
    model.addAttribute("title", "공지 수정");
    
    return "board/noticeEdit";
  }

  // 공지 수정 처리
  @PostMapping("/notice/{boardId}")
  public String updateNotice(@PathVariable("boardId") Long boardId, Board form) {
    
    Board updated = boardService.updatePost(boardId, form);
    
    return "redirect:/board/notice/" + updated.getBoardId();
  }

  // 부서 수정 폼
  @GetMapping("/department/{boardId}/edit")
  public String departmentEditForm(@PathVariable("boardId") Long boardId, Model model) {
    
    Board post = boardService.getBoardsDetail(boardId);
    
    model.addAttribute("post", post);
    model.addAttribute("title", "부서 글 수정");
    
    return "board/departmentEdit";
  }

  // 부서 수정 처리
  @PostMapping("/department/{boardId}")
  public String updateDepartment(@PathVariable("boardId") Long boardId, Board form) {
    
    Board updated = boardService.updatePost(boardId, form);
    
    return "redirect:/board/department/" + updated.getBoardId();
  }

  // 공지 삭제
  @PostMapping("/notice/{boardId}/delete")
  public String deleteNotice(@PathVariable("boardId") Long boardId) {

    boardService.deletePost(boardId);

    return "redirect:/board/notice";
  }

  // 부서글 삭제
  @PostMapping("/department/{boardId}/delete")
  public String deleteDepartment(@PathVariable("boardId") Long boardId) {

    boardService.deletePost(boardId);
    
    return "redirect:/board/department";
  }

}
