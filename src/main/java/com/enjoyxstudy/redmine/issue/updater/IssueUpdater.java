package com.enjoyxstudy.redmine.issue.updater;

import java.io.IOException;
import java.util.List;

import com.enjoyxstudy.redmine.issue.updater.client.Client;

import lombok.Value;

@Value
public class IssueUpdater {

    private final Client client;

    public IssueId update(PrimaryKey key, Issue issue) throws IOException {

        // キーとなる情報を使ってIssueを検索
        List<Issue> targetIssues = client.getIssues(key.getQueryParameter());

        // 1件ではない場合はエラー
        if (targetIssues.size() == 0) {
            throw new IllegalStateException("The target issue was not found. " + key);
        } else if (targetIssues.size() > 1) {
            throw new IllegalStateException("There are multiple target issue. " + key);
        }

        int targetIssueId = targetIssues.get(0).getId();
        issue.setId(targetIssueId);

        // 内容更新
        client.updateIssue(issue);

        // 更新対象となったIssueIdを返却
        return new IssueId(targetIssueId);
    }
}