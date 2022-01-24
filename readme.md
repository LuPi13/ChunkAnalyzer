# ChunkAnalyzer

/analyze [숫자; 분석할 청크 수] [true/false; 맵 파괴 여부]:

    자신이 속한 청크를 중심으로 입력한 숫자만큼 청크의 블럭 개수 분석
    true일 시, exception 등록을 하지 않은 블록을 모두 파괴


/exception [add/remove] [blockcode/all(remove일때만)]:

    /analyze n true 입력 시, exception add로 등록 된 블럭은 파괴되지 않음
    /analyze remove all 입력 시, 예외 모두 제거
    config.yml 에서도 수정 가능


/emphasize [blockcode/remove]:

    /emphasize 등록한 블럭은 y좌표 별로 분석이 추가로 이루어짐
    /emphasize remove 로 제거
    config.yml 에서도 수정 가능
