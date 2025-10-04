package com.example.highloadsystemswardrobemanager.service;

import java.util.List;

/**
 * Универсальная обёртка для постраничного вывода (pagination)
 * Хранит список элементов и общее количество записей.
 */
public record PagedResult<T>(List<T> items, long totalCount) {}
