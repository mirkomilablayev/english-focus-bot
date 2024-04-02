package com.ielts.englishfocusbot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemPageService {



    private final InlineButtonService inlineButtonService;


//    public InlineKeyboardMarkup task1page(int page, User user) {
//        List<WritingTask1> writingTask1List = writingTask1Repository.findAllTaskOnePageAble((page - 1) * 10, 10);
//        long count = writingTask1Repository.count();
//        count = pageCount(count);
//        int order = getOrder(page);
//        List<Task1BuildPageDto> list = new ArrayList<>();
//        for (WritingTask1 writingTask1 : writingTask1List) {
//            boolean isOpen = user.getIsPremium() || order < 5;
//            list.add(new Task1BuildPageDto(writingTask1.getId(), order, isOpen));
//            order++;
//        }
//        return inlineButtonService.generateInlineButtons(list, count, page, "task1");
//    }
//
//
//    public InlineKeyboardMarkup task2page(int page, User user) {
//        List<WritingTask2> writingTask2List = writingTask2Repository.findAllTaskOnePageAble((page - 1) * 10, 10);
//        long count = writingTask2Repository.count();
//        count = count % 10 > 0 ? count / 10 + 1 : count / 10;
//        int order = page == 1 ? 1 : ((page - 1) * 10 + 1);
//        List<Task1BuildPageDto> list = new ArrayList<>();
//        for (WritingTask2 writingTask2 : writingTask2List) {
//            list.add(new Task1BuildPageDto(writingTask2.getId(), order, (user.getIsPremium() || order < 5)));
//            order++;
//        }
//        return inlineButtonService.generateInlineButtons(list, count, page, "task2");
//    }
//
//
//    public InlineKeyboardMarkup writingBooks(int page, User user) {
//        List<WritingBook> writingBookList = writingBookRepository.findAllWritingBooks((page - 1) * 10, 10);
//        long count = writingBookRepository.count();
//        long pageCount = pageCount(count);
//        int order = getOrder(page);
//
//
//        List<Task1BuildPageDto> list = new ArrayList<>();
//        for (WritingBook writingBook : writingBookList) {
//            Task1BuildPageDto task1BuildPageDto = new Task1BuildPageDto();
//            task1BuildPageDto.setId(writingBook.getId());
//            task1BuildPageDto.setIsOpen(user.getIsPremium() || order < 5);
//            task1BuildPageDto.setOrder(order);
//            list.add(task1BuildPageDto);
//            order++;
//        }
//        return inlineButtonService.generateInlineButtons(list, pageCount, page, "writingBooks");
//    }
//


    private Long pageCount(Long count) {
        return count % 10 > 0 ? count / 10 + 1 : count / 10;
    }

    private int getOrder(int page) {
        return page == 1 ? 1 : ((page - 1) * 10 + 1);
    }

}
