package com.yiowoc.community.service;

import com.yiowoc.community.dto.NotificationDTO;
import com.yiowoc.community.dto.PaginationDTO;
import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.enums.NotificationStatusEnum;
import com.yiowoc.community.enums.NotificationTypeEnum;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.exception.CustomizeException;
import com.yiowoc.community.mapper.NotificationMapper;
import com.yiowoc.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;

    public PaginationDTO selectNotificationDTOsByUserId(Integer userId, Integer curPage, Integer size) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        if(notifications != null && notifications.size() != 0) {
            for(Notification notification: notifications) {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification, notificationDTO);
                notificationDTO.setTypeName(NotificationTypeEnum.notificationOfType(notification.getType()));
                notificationDTOs.add(notificationDTO);
            }
        }
        paginationDTO.setData(notificationDTOs);
        return paginationDTO;
    }

    public Long getUnreadCount(Integer id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                        .andReceiverEqualTo(id)
                                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        // 进行简单的校验
        if(notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKeySelective(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.notificationOfType(notificationDTO.getType()));
        return notificationDTO;
    }
}
