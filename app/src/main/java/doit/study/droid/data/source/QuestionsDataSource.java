package doit.study.droid.data.source;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface QuestionsDataSource {
    Maybe<List<Question>> getQuestions();
    Completable saveQuestions(List<Question> questions);
    Completable populateDb();
}