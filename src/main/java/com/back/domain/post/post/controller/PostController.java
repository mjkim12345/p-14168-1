package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ModelAttribute("siteName")
    public String siteName() {
        return "커뮤니티 사이트 A";
    }


    @AllArgsConstructor
    @Getter
    @Setter
    public static class ModifyForm {
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "02-title-제목은 2자 이상, 20자 이하로 입력가능합니다.")
        private String title;
        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "04-content-내용은 2자 이상, 20자 이하로 입력가능합니다.")
        private String content;
    }

    @GetMapping("/posts/{id}/modify")
    @Transactional(readOnly = true)
    public String showModify(
            @PathVariable int id,
            @ModelAttribute("form") ModifyForm form,
            Model model
    ) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());

        return "post/post/modify";
    }

    @PostMapping("/posts/{id}/modify")
    @Transactional
    public String modify(
            @PathVariable int id,
            @Valid @ModelAttribute("form") ModifyForm form,
            BindingResult bindingResult,
            Model model
    ) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);

        if (bindingResult.hasErrors()) {
            return "post/post/modify";
        }

        postService.modify(post, form.getTitle(), form.getContent());

        return "redirect:/posts/" + post.getId();
    }


    @AllArgsConstructor
    @Getter
    public static class WriteForm {
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "02-title-제목은 2자 이상, 20자 이하로 입력가능합니다.")
        private String title;
        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "04-content-내용은 2자 이상, 20자 이하로 입력가능합니다.")
        private String content;
    }

    @GetMapping("/posts/write")
    public String showWrite(@ModelAttribute("form") WriteForm form) {
        return "post/post/write";
    }

    @PostMapping("/posts/write")
    @Transactional
    public String write(
            @ModelAttribute("form") @Valid WriteForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) return "post/post/write";

        Post post = postService.write(form.getTitle(), form.getContent());

        return "redirect:/posts/" + post.getId();
    }


    @GetMapping("/posts/{id}")
    @Transactional(readOnly = true)
    public String showDetail(
            @PathVariable int id,
            Model model
    ) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "post/post/detail";
    }

    @GetMapping("/posts")
    @Transactional(readOnly = true)
    public String showList(Model mod) {
        List<Post> posts = postService.findAll();

        mod.addAttribute("posts", posts);

        return "post/post/list";
    }

    @GetMapping("/posts/listByFetch")
    public String showListByFetch() { // 템플릿 불러오는 함수
        return "post/post/listByFetch";
    }

    @GetMapping("/posts/")
    public String redirectToList() {
        return "redirect:/posts";
    }
}