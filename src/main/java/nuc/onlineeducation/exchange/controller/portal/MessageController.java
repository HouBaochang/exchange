package nuc.onlineeducation.exchange.controller.portal;

import com.github.pagehelper.PageInfo;
import nuc.onlineeducation.exchange.common.Const;
import nuc.onlineeducation.exchange.common.ResponseCodeEnum;
import nuc.onlineeducation.exchange.common.ServerResponse;
import nuc.onlineeducation.exchange.model.HostHolder;
import nuc.onlineeducation.exchange.model.Message;
import nuc.onlineeducation.exchange.model.User;
import nuc.onlineeducation.exchange.service.IMessageService;
import nuc.onlineeducation.exchange.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ji YongGuang.
 * @date 1:06 2018/1/10.
 */
@RestController
@RequestMapping(value = "/messages")
public class MessageController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    IMessageService iMessageService;

    @Autowired
    IUserService iUserService;

    /**
     * 获取会话列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ServerResponse<PageInfo> getConversationList(@RequestParam(value = "pageNum", defaultValue = "0") Integer
                                                                pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer
                                                                pageSize) {
        if (hostHolder.getUser() == null) {
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        int localUserId = hostHolder.getUser().getId();
        return iMessageService.getConversationList(localUserId, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    public ServerResponse<PageInfo> getConversationDetail(@PathVariable(value = "id") String conversationId,
                                                          @RequestParam(value = "pageNum", defaultValue = "0")
                                                                  Integer pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer
                                                                  pageSize) {
        return iMessageService.getConversationDetail(conversationId, pageNum, pageSize);
    }

    @PostMapping("/add")
    public ServerResponse messageSave(@RequestParam("toName") String toName,
                                      @RequestParam("content") String content) {
        if (hostHolder.getUser() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum
                    .NEED_LOGIN.getDesc());
        }

        User user = iUserService.selectByUsername(toName).getData();
        if (user == null) {
            return ServerResponse.createByErrorMessage("该用户不存在");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(user.getId());
        message.setContent(content);
        message.setHasRead(Const.MessageStatus.UN_READ);
        return iMessageService.saveMessage(message);
    }
}