package com.chordncode.springfileserver.file.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chordncode.springfileserver.data.dto.FileDto;
import com.chordncode.springfileserver.file.service.IFileService;
import com.chordncode.springfileserver.util.PageInfo;
import com.chordncode.springfileserver.util.ResultStatus;

@Controller
@RequestMapping("/file")
public class FileController {
    
    private IFileService fileService;
    public FileController(IFileService fileService){
        this.fileService = fileService;
    }

    @GetMapping("")
    public String home(@RequestParam(required = false) String currentDir, @RequestParam(required = false) Long page, Model model){
        PageInfo<FileDto> pageInfo = fileService.list(page);
        model.addAttribute("pageInfo", pageInfo);
        return "file/list";
    }

    @ResponseBody
    @GetMapping("/{fileId}")
    public String select(@PathVariable Long fileId){
        return fileService.select(fileId);
    }

    @GetMapping("/upload")
    public String upload(String currentDir, Model model){
        model.addAttribute("currentDir", currentDir);
        return "file/upload";
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile[] uploadFile){
        ResultStatus result = null;
        try {
            result = fileService.upload(uploadFile);   
            return result.getStatus();
        } catch (Exception e) {}
        return ResultStatus.FAILED.getStatus();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId){
        try {
            return fileService.download(fileId);
        } catch (Exception e) {}
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long fileId, Model model){
        try {
            fileService.delete(fileId);
            return "redirect:/file";
        } catch (Exception e) {}
        model.addAttribute("msg", "잠시 후 다시 시도해주세요.");
        return "alert/alertBack";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute FileDto fileDto, HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            fileService.update(fileDto);
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer);
            return null;
        } catch (Exception e) {}
        model.addAttribute("msg", "잠시 후 다시 시도해주세요");
        return "alert/alertBack";
    }

    @PostMapping("/newDir")
    public String createNewDir(@RequestParam(required = false, defaultValue = "/")String currentDir){
        return null;
    }
}
